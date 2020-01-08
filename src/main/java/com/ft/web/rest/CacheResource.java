package com.ft.web.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hazelcast.core.HazelcastInstance;

@Controller
@RequestMapping("/api")
public class CacheResource {

	private final Logger log = LoggerFactory.getLogger(CacheResource.class);
	
	@Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    HazelcastInstance hazelcastInstance;
    
    /**
     * Generate a flake id
     * @param index
     * @return
     */
    @GetMapping("/flake-id/{index}")
	public ResponseEntity<Long> getFlakeId(@PathVariable String index) {
    	return ResponseEntity.ok(hazelcastInstance.getFlakeIdGenerator(index).newId());
	}
    
    /**
     * Delete flake id
     * @param index
     * @return
     */
    @DeleteMapping("/flake-id/{index}")
	public ResponseEntity<Void> resetFlakeId(@PathVariable String index) {
    	hazelcastInstance.getFlakeIdGenerator(index).destroy();
    	return ResponseEntity.noContent().build();
	}
    
    /**
     * Generate an Atomic Long Counter
     * @param index
     * @return
     */
    @GetMapping("/atomic-id/{index}")
	public ResponseEntity<Long> getAtomicId(@PathVariable String index) {
    	return ResponseEntity.ok(hazelcastInstance.getAtomicLong(index).getAndIncrement());
	}
    
    @PutMapping("/atomic-id/{index}/{newValue}")
	public ResponseEntity<Long> setAtomicId(@PathVariable String index, @PathVariable long newValue) {
    	hazelcastInstance.getAtomicLong(index).set(newValue);
    	return ResponseEntity.ok(newValue);
	}
    
    /**
     * Delete Atomic ID
     * @param index
     * @return
     */
    @DeleteMapping("/atomic-id/{index}")
	public ResponseEntity<Void> resetAtomicId(@PathVariable String index) {
    	hazelcastInstance.getAtomicLong(index).destroy();
    	return ResponseEntity.noContent().build();
	}
    
    /**
     * Counter API, get current value then add cnt
     * @param index
     * @param cnt
     * @return
     */
    @GetMapping("/counter/{index}/{cnt}")
	public ResponseEntity<Long> getCounterId(@PathVariable String index, @PathVariable long cnt) {
    	return ResponseEntity.ok(hazelcastInstance.getPNCounter(index).addAndGet(cnt));
	}
    
    @PutMapping("/counter/{index}/{cnt}")
	public ResponseEntity<Long> setCounterId(@PathVariable String index, @PathVariable long cnt) {
    	hazelcastInstance.getPNCounter(index).destroy();
    	hazelcastInstance.getPNCounter(index).getAndAdd(cnt);
    	return ResponseEntity.ok(cnt);
	}
    
    @DeleteMapping("/counter/{index}/{cnt}")
	public ResponseEntity<Long> getDecreaseId(@PathVariable String index, @PathVariable long cnt) {
    	return ResponseEntity.ok(hazelcastInstance.getPNCounter(index).getAndSubtract(cnt));
	}
    
    /**
     * Delete Atomic ID
     * @param index
     * @return
     */
    @DeleteMapping("/counter/{index}")
	public ResponseEntity<Void> resetCounterId(@PathVariable String index) {
    	hazelcastInstance.getPNCounter(index).destroy();
    	return ResponseEntity.noContent().build();
	}
    
    
    /**
     * Generate an universal unique ID
     * @param index
     * @return
     */
    @GetMapping("/uuid")
    public ResponseEntity<String> getStringId() {
    	return ResponseEntity.ok(UUID.randomUUID().toString());
	}
}
