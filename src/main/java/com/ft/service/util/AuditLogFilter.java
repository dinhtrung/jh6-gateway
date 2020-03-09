
package com.ft.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.ft.domain.PersistentAuditEvent;
import com.ft.repository.PersistenceAuditEventRepository;
import com.ft.security.SecurityUtils;

/**
 * https://github.com/librucha/servlet-logging-filter/blob/master/src/main/java/javax/servlet/filter/logging/LoggingFilter.java
 */
public class AuditLogFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(AuditLogFilter.class);
	
	PersistenceAuditEventRepository auditRepo;
	
    public AuditLogFilter(PersistenceAuditEventRepository auditRepo) {
		super();
		this.auditRepo = auditRepo;
	}


    /** {@inheritDoc} */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	// Nothing to init
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Nothing to destroy
    }

    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    	
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

    	if (!(httpRequest instanceof ContentCachingRequestWrapper)) {
            httpRequest = new ContentCachingRequestWrapper(httpRequest);
        }
        if (!(httpResponse instanceof ContentCachingResponseWrapper)) {
        	httpResponse = new ContentCachingResponseWrapper(httpResponse);
        }

        try {
            chain.doFilter(httpRequest, httpResponse);
        } finally {
            log(httpRequest, httpResponse);
            updateResponse(httpResponse);
        }
    }

    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache) {
    	String login = SecurityUtils.getCurrentUserLogin().orElse(null);
    	String method = requestToCache.getMethod();
    	String contentType = responseToCache.getContentType();
    	boolean isGzipped = responseToCache.getHeaders(HttpHeaders.CONTENT_ENCODING).stream().filter(i -> i.contains("gzip")).findAny().isPresent();
    	
    	
        if (HttpMethod.DELETE.matches(method) || HttpMethod.PUT.matches(method)
                || HttpMethod.PATCH.matches(method) || HttpMethod.POST.matches(method)
        // Only log those with content type text or json
        && (contentType.contains("text") || contentType.contains("json"))
        // Only log success request
        && (responseToCache.getStatus() / 100 == 2)) {
        	PersistentAuditEvent audit = new PersistentAuditEvent();
        	audit.setAuditEventDate(Instant.now());
        	audit.setPrincipal(login);
        	audit.setAuditEventType(method + " " + requestToCache.getRequestURI());
        	
        	Map<String, String> log = new HashMap<>();
        	log.put("responseCode", responseToCache.getStatus() + "");
        	String responsePayload = getResponsePayload(responseToCache, isGzipped);
        	log.put("responsePayload", responsePayload);
        	log.put("remoteAddress", extractRemoteAddress(requestToCache));
//        	// Quick and dirty way to translate message
//        	String code = method + "-" + requestToCache.getRequestURI().replace("/", "_");
//        	String message = props.msg(code, audit.getAuditEventType(), log);
//        	
//        	try {
//				message = props.msg(code, audit.getAuditEventType(), responsePayload == null ? null : JsonUtil.toPlaceholders(responsePayload));
//			} catch (IOException e) {
//			}
//        	
//        	log.put("message", message);
        	audit.setData(log);
        	auditRepo.save(audit);
            logger.info("AUDIT: {}", log);
        }
    }
    
    /**
     * Try to extract the paramters from client
     * @param requestToCache
     * @return
     */
	protected String extractRemoteAddress(HttpServletRequest requestToCache) {
		logger.debug("header names: {}", requestToCache.getHeaderNames());
		List<String> ipHeaders = Arrays.asList("X-FORWARDED-FOR", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED");
		String remoteAddress = requestToCache.getRemoteAddr();
		for (String h : ipHeaders) {
			if (requestToCache.getHeader(h) != null) {
				remoteAddress = requestToCache.getHeader(h);
				break;
			}
		}
		return remoteAddress;
	}

    private String getResponsePayload(HttpServletResponse response, boolean gzipped) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {

        	if (gzipped) {
                InputStream responseDataStream;
				try {
					responseDataStream = new GZIPInputStream(wrapper.getContentInputStream());
					return IOUtils.toString(responseDataStream, wrapper.getCharacterEncoding());
				} catch (IOException e) {
					
				}
                
            } else {
            	byte[] buf = wrapper.getContentAsByteArray();
            	if (buf.length > 0) {
            		int length = Math.min(buf.length, 5120);
            		try {
            			return new String(buf, 0, length, wrapper.getCharacterEncoding());
            		}
            		catch (UnsupportedEncodingException ex) {
            			// NOOP
            		}
            	}
            }
        }
        return null;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
            WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }
}
