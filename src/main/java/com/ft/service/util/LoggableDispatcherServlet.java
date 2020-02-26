package com.ft.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.ft.domain.PersistentAuditEvent;
import com.ft.repository.PersistenceAuditEventRepository;
import com.ft.security.SecurityUtils;

public class LoggableDispatcherServlet extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(LoggableDispatcherServlet.class);

	PersistenceAuditEventRepository auditRepo;
	
    public LoggableDispatcherServlet(PersistenceAuditEventRepository auditRepo) {
		super();
		this.auditRepo = auditRepo;
	}

	@Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        HandlerExecutionChain handler = getHandler(request);

        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response, handler);
            updateResponse(response);
        }
    }

    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler) {
    	String login = SecurityUtils.getCurrentUserLogin().orElse(null);
    	String method = requestToCache.getMethod();
    	String contentType = responseToCache.getContentType();
    	
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
        	log.put("responsePayload", getResponsePayload(responseToCache));
        	log.put("remoteAddress", requestToCache.getRemoteAddr());
        	
        	audit.setData(log);
        	auditRepo.save(audit);
            logger.debug("AUDIT: {}", log);
        }
    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {

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
        return "[unknown]";
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
            WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }
}
