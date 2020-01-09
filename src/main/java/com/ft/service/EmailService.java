package com.ft.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.PropertyPlaceholderHelper;

import io.github.jhipster.config.JHipsterProperties;

public class EmailService {
	
	private final Logger log = LoggerFactory.getLogger(EmailService.class);
	
	private static final String BASE_URL = "baseUrl";
	private static final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");

	@Autowired
	JHipsterProperties jHipsterProperties;
	
	@Autowired
	JavaMailSender javaMailSender;

    /**
     * Send Email to user with custom content
     * @param to
     * @param subject
     * @param content
     * @param isMultipart
     * @param isHtml
     * @param placeholders
     */
	@Async
	public void sendEmail(
			String to, 
			String subject, 
			String content, 
			boolean isMultipart, 
			boolean isHtml,
			Map<String, Object> placeholders
	) {
		log.debug(
				"Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={} with placeholder={}",
				isMultipart, isHtml, to, subject, content, placeholders);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			
			Properties properties = new Properties();
			properties.putAll(placeholders);
			properties.put(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
			String messageText = propertyPlaceholderHelper.replacePlaceholders(content, properties);
			message.setText(messageText);
			log.debug("Sent email to User '{}': {}", to, messageText);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
		}
	}
}
