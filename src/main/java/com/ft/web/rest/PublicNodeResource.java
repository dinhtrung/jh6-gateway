package com.ft.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ft.domain.Node;
import com.ft.repository.NodeRepository;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ft.domain.Node}.
 */
@RestController
@RequestMapping("/api/public")
public class PublicNodeResource {

	private final Logger log = LoggerFactory.getLogger(PublicNodeResource.class);

	private static final String ENTITY_NAME = "node";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final NodeRepository nodeRepository;

	public PublicNodeResource(NodeRepository nodeRepository) {
		this.nodeRepository = nodeRepository;
	}

	/**
	 * {@code GET  /nodes} : get all the nodes.
	 *
	 * 
	 * @param pageable the pagination information.
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of nodes in body.
	 */
	@GetMapping("/nodes")
	public ResponseEntity<List<Node>> getAllEntries(
			@RequestParam(required = false) String query, 
			Pageable pageable) {
		log.info("REST request to get a page of Nodes, predicate: {}", query);
		Page<Node> page = query == null ? nodeRepository.findAll(pageable)
				: nodeRepository.search(simpleQueryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /nodes/:id} : get the "id" node.
	 *
	 * @param id the id of the node to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the node, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/nodes/{id}")
	public ResponseEntity<Node> getNode(@PathVariable String id) {
		log.debug("REST request to get Node : {}", id);
		Optional<Node> node = nodeRepository.findById(id).filter(i -> (i.getState() / 100) == 2);
		return ResponseUtil.wrapOrNotFound(node);
	}
}
