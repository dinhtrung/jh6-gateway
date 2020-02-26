package com.ft.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ft.service.util.HeaderRequestInterceptor;

/**
 * Configure default rest template to enforce content type and log request and response
 * @author dinhtrung
 *
 */
@Configuration
public class RestTemplateConfiguration {

	/**
	 * Create a new REST Template to connect to other services
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate(factory);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		restTemplate.getInterceptors().add(new HeaderRequestInterceptor(headers));
		restTemplate.getMessageConverters().clear();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}
}
