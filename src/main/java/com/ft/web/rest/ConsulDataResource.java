package com.ft.web.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.security.AuthoritiesConstants;

@RestController
@RequestMapping("/api/consul")
public class ConsulDataResource {

	@Autowired
	ConsulClient consulClient;

	private YamlMapFactoryBean yamlMapper = new YamlMapFactoryBean();

	private ObjectMapper objectMapper = new ObjectMapper();

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
	 * @throws IOException
	 */
	@GetMapping("/config")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<String> getConfiguration(@RequestParam String key, @RequestParam(required = false) String file, @RequestParam(required = false) String format) throws IOException {
		String content = consulClient.getKVValue(key).getValue().getDecodedValue();
		if (file != null) {
			try {
				Resource res = new ByteArrayResource(content.getBytes());
				yamlMapper.setResources(res);
				Map<String, Object> data = yamlMapper.getObject();
				if (format.equalsIgnoreCase("json")) {
					content = objectMapper.writeValueAsString(data);
				}
			} catch (Exception e) {
			}
			File fileResource = new File(file);
			FileUtils.writeStringToFile(fileResource, content, StandardCharsets.UTF_8);
		}
		return ResponseEntity.ok(content);
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
		return ResponseEntity.ok(consulClient.setKVValue(key, value).getValue() ? value : null);
	}
	/**
	 * Delete one key
	 * @param key
	 * @return
	 */
	@DeleteMapping("/config")
    @Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteConfiguration(@RequestParam String key) {
		return ResponseEntity.ok(consulClient.deleteKVValues(key).getValue());
	}
}
