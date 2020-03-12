package com.ft.service.oam;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.Check;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Springboot Health check via Consul
 * @author dinhtrung
 *
 */

@Service
public class OamHealthCheck {

	private final Logger log = LoggerFactory.getLogger(OamHealthCheck.class);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ConsulClient consulClient;
	
	@Scheduled(fixedRate = 10000)
	public void performHealthCheckViaConsul() {
		Map<String, List<String>> catalogServices = consulClient.getCatalogServices(null).getValue();
		log.debug("Perform health check for services: {}", catalogServices);
		
		List<Check> healthCheckState = consulClient.getHealthChecksState(null).getValue();
		log.debug("Perform health check for services: {}", healthCheckState);
		
		
	}
}
