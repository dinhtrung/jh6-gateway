package com.ft.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ft.security.AuthoritiesConstants;

@RestController
@RequestMapping("/api/consul")
public class ConsulConfigurationResource {

	@Autowired
	ConsulClient consulClient;
	
	/**
	 * List all available configuration keys
	 * @return
	 */
	@GetMapping("/configs")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<List<String>> getConfigurations() {
		return ResponseEntity.ok(consulClient.getKVKeysOnly("config").getValue());
	}
	
	/**
	 * Retrieve one key in decoded value
	 * @param key
	 * @return
	 */
	@GetMapping("/config")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<String> getConfiguration(@RequestParam String key) {
		return ResponseEntity.ok(consulClient.getKVValue("config/" + key).getValue().getDecodedValue());
	}
	
	/**
	 * Update the key with target YAML or JSON
	 * @param key
	 * @param value
	 * @return
	 */
	@PostMapping("/config")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<String> setConfiguration(@RequestParam String key, @RequestBody String value) {
		return ResponseEntity.ok(consulClient.setKVValue("config/" + key, value).getValue() ? value : null);
	}
	
	/**
	 * Delete one key
	 * @param key
	 * @return
	 */
	@DeleteMapping("/config")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteConfiguration(@RequestParam String key) {
		return ResponseEntity.ok(consulClient.deleteKVValues("config/" + key).getValue());
	}
}
