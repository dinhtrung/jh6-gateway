package com.ft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ft.domain.Node;
import com.ft.repository.NodeRepository;
import com.ft.security.AuthoritiesConstants;
import com.ft.security.SecurityUtils;
import com.ft.service.util.QuerydslPredicateUtil;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ft.domain.Node}.
 */
@RestController
@RequestMapping("/api")
public class NodeResource {

	private final Logger log = LoggerFactory.getLogger(NodeResource.class);

	private static final String ENTITY_NAME = "node";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final NodeRepository nodeRepository;

	public NodeResource(NodeRepository nodeRepository) {
		this.nodeRepository = nodeRepository;
	}

	/**
	 * {@code POST  /nodes} : Create a new node.
	 *
	 * @param node the node to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new node, or with status {@code 400 (Bad Request)} if the
	 *         node has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/nodes")
	public ResponseEntity<Node> createNode(@Valid @RequestBody Node node) throws URISyntaxException {
		log.debug("REST request to save Node : {}", node);
		if (node.getId() != null) {
			throw new BadRequestAlertException("A new node cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Node result = nodeRepository.save(node);
		return ResponseEntity
				.created(new URI("/api/nodes/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /nodes} : Updates an existing node.
	 *
	 * @param node the node to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated node, or with status {@code 400 (Bad Request)} if the
	 *         node is not valid, or with status {@code 500 (Internal Server Error)}
	 *         if the node couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/nodes")
	public ResponseEntity<Node> updateNode(@Valid @RequestBody Node node) throws URISyntaxException {
		log.debug("REST request to update Node : {}", node);
		if (node.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Node result = nodeRepository.save(node);
		return ResponseEntity.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, node.getId().toString()))
				.body(result);
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
			Predicate predicate, 
			Pageable pageable,
		    @RequestParam(required=false) Map<String, Object> params) {
		if (params != null) {
			List<Predicate> paramPredicate = QuerydslPredicateUtil.toPredicates(null, params);
			log.debug("Got predicate: {}", paramPredicate);
			BooleanBuilder finalPredicate = new BooleanBuilder();
			paramPredicate.forEach(i -> finalPredicate.and(i));
			if (predicate != null)
				finalPredicate.and(predicate);
			predicate = finalPredicate.getValue();
		}
		log.info("REST request to get a page of Nodes, predicate: {}, params: {}", predicate, params);
		Page<Node> page = predicate == null ? nodeRepository.findAll(pageable)
				: nodeRepository.findAll(predicate, pageable);
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
		Optional<Node> node = nodeRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(node);
	}

	/**
	 * {@code DELETE  /nodes/:id} : delete the "id" node.
	 *
	 * @param id the id of the node to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/nodes/{id}")
	public ResponseEntity<Void> deleteNode(@PathVariable String id) {
		log.debug("REST request to delete Node : {}", id);
		nodeRepository.deleteById(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
	}
	
	
	@PutMapping("/import/nodes/{type}")
	@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
	public ResponseEntity<Set<Node>> importNode(@PathVariable String type, @Valid @RequestBody List<Node> node) throws URISyntaxException {
		log.debug("REST request to import Node : {}", node);
		String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid login"));
		return ResponseEntity.ok().body(
			node.parallelStream()
			.map(n -> {
				try {
					return nodeRepository.save(
							n.type(type)
							.updatedAt(Instant.now())
							.updatedBy(login)
							.touchedBy(login)
					);
				} catch (Exception e) {
					log.warn("Cannot process node: {}", n, e);
					return null;
				}
			})
			.filter(i -> i != null)
			.distinct().collect(Collectors.toSet())
		);
	}
}
