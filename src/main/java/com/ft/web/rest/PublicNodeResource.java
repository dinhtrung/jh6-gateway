package com.ft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Node;
import com.ft.domain.QNode;
import com.ft.repository.NodeRepository;
import com.ft.service.util.QuerydslPredicateUtil;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

import io.github.jhipster.web.util.HeaderUtil;
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
			Predicate predicate, 
			Pageable pageable,
		    @RequestParam(required=false) Map<String, Object> params) {
		if (params != null) {
			List<Predicate> paramPredicate = QuerydslPredicateUtil.toPredicates(null, params);
			log.debug("Got predicate: {}", paramPredicate);
			BooleanBuilder finalPredicate = new BooleanBuilder();
			finalPredicate.and(QNode.node.state.between(199, 300));
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
		Optional<Node> node = nodeRepository.findById(id).filter(i -> (i.getState() / 100) == 2);
		return ResponseUtil.wrapOrNotFound(node);
	}
}
