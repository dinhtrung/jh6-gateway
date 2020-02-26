package com.ft.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.DispatcherServlet;

import com.ft.repository.PersistenceAuditEventRepository;
import com.ft.service.util.LoggableDispatcherServlet;

import io.github.jhipster.config.JHipsterConstants;


@Configuration
@Profile({ Constants.SPRING_PROFILE_AUDIT, JHipsterConstants.SPRING_PROFILE_DEVELOPMENT })
public class AuditConfiguration {

    private final Logger log = LoggerFactory.getLogger(AuditConfiguration.class);

    /**
     * Enable logging of request and responses
     */
        
    @Autowired
    PersistenceAuditEventRepository persistAuditEventRepo;
    
    @PostConstruct
    public void init() {
    	log.info("+ Audit is enabled. All success response with POST, PUT and DELETE methods are stored in DB for reference later");
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration() {
        return new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet());
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        return new LoggableDispatcherServlet(persistAuditEventRepo);
    }
}
