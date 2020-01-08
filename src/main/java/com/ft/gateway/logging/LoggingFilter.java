package com.ft.gateway.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.security.SecurityUtils;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter;
import org.springframework.http.HttpMethod;

import springfox.documentation.swagger2.web.Swagger2Controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Log all POST PUT DELETE PATCH request send to microservices
 */
public class LoggingFilter extends SendResponseFilter {

    private final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    private ObjectMapper mapper = new ObjectMapper();
    
    private String excludePath;

    public LoggingFilter(String excludePath) {
        super(new ZuulProperties());
        this.excludePath = excludePath;
        log.debug("Exclude pattern: {}", excludePath);
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 999;
    }

    /**
     * Filter requests to micro-services Swagger docs.
     */
    @Override
    public boolean shouldFilter() {
    	String path = RequestContext.getCurrentContext().getRequest().getRequestURI();
    	log.debug("Compare path {}", path);
    	if (Stream.of(StringUtils.splitByWholeSeparatorPreserveAllTokens(excludePath, null)).anyMatch(prefix -> path.startsWith(prefix))) {
    		String method = RequestContext.getCurrentContext().getRequest().getMethod();
    		return SecurityUtils.getCurrentUserLogin().isPresent() && (HttpMethod.DELETE.matches(method) || HttpMethod.PUT.matches(method) || HttpMethod.PATCH.matches(method) || HttpMethod.POST.matches(method)) && (RequestContext.getCurrentContext().getResponse().getStatus() / 100 == 2);
    	}
    	return false;
    }

    @Override
    public Object run() {
    	RequestContext context = RequestContext.getCurrentContext();
    	String login = SecurityUtils.getCurrentUserLogin().get();
        context.getResponse().setCharacterEncoding("UTF-8");

        String rewrittenResponse = rewriteBasePath(context);
        if (context.getResponseGZipped()) {
            try {
                context.setResponseDataStream(new ByteArrayInputStream(gzipData(rewrittenResponse)));
            } catch (IOException e) {
                log.error("Swagger-docs filter error", e);
            }
        } else {
            context.setResponseBody(rewrittenResponse);
        }
        
        try  {
    		String request = IOUtils.toString(context.getRequest().getInputStream(), StandardCharsets.UTF_8);
    		log.info(">> {} | {}", login, request);
			log.info("<< {} | {}", context.getResponseStatusCode(), rewrittenResponse);
		} catch (IOException e1) {
			log.error("Cannot rewrite data: {}", e1.getMessage(), e1);
		}
        return null;
    }

    @SuppressWarnings("unchecked")
    private String rewriteBasePath(RequestContext context) {
        InputStream responseDataStream = context.getResponseDataStream();
//        String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        try {
            if (context.getResponseGZipped()) {
                responseDataStream = new GZIPInputStream(context.getResponseDataStream());
            }
            return IOUtils.toString(responseDataStream, StandardCharsets.UTF_8);
//            if (response != null) {
//                LinkedHashMap<String, Object> map = this.mapper.readValue(response, LinkedHashMap.class);
//
//                String basePath = requestUri.replace(Swagger2Controller.DEFAULT_URL, "");
//                map.put("basePath", basePath);
//                log.debug("Swagger-docs: rewritten Base URL with correct micro-service route: {}", basePath);
//                return mapper.writeValueAsString(map);
//            }
        } catch (IOException e) {
            log.error("Swagger-docs filter error", e);
        }
        return null;
    }

    public static byte[] gzipData(String content) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter gzip = new PrintWriter(new GZIPOutputStream(bos));
        gzip.print(content);
        gzip.flush();
        gzip.close();
        return bos.toByteArray();
    }
}
