package com.ft.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.ft.domain.Category;
import com.ft.repository.CategoryRepository;
import com.ft.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ft.domain.Category}.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

	private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

	private static final String ENTITY_NAME = "category";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final CategoryRepository categoryRepository;

	public CategoryResource(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	/**
	 * {@code POST  /categorys} : Create a new category.
	 *
	 * @param category the category to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new category, or with status {@code 400 (Bad Request)} if the
	 *         category has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/categorys")
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException {
		log.debug("REST request to save Category : {}", category);
		if (category.getId() != null) {
			throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Category result = categoryRepository.save(category);
		return ResponseEntity
				.created(new URI("/api/categorys/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /categorys} : Updates an existing category.
	 *
	 * @param category the category to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated category, or with status {@code 400 (Bad Request)} if the
	 *         category is not valid, or with status {@code 500 (Internal Server Error)}
	 *         if the category couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/categorys")
	public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) throws URISyntaxException {
		log.debug("REST request to update Category : {}", category);
		if (category.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Category result = categoryRepository.save(category);
		return ResponseEntity.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, category.getId().toString()))
				.body(result);
	}

	/**
	 * {@code GET  /categorys} : get all the categorys.
	 *
	 * 
	 * @param pageable the pagination information.
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of categorys in body.
	 */
	@GetMapping("/categorys")
	public ResponseEntity<List<Category>> getAllEntries(
			@RequestParam(required = false) String query, 
			Pageable pageable) {
		log.info("REST request to get a page of Categorys, predicate: {}", query);
		Page<Category> page = query == null ? categoryRepository.findAll(pageable)
				: categoryRepository.search(simpleQueryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil
				.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * {@code GET  /categorys/:id} : get the "id" category.
	 *
	 * @param id the id of the category to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the category, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/categorys/{id}")
	public ResponseEntity<Category> getCategory(@PathVariable String id) {
		log.debug("REST request to get Category : {}", id);
		Optional<Category> category = categoryRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(category);
	}

	/**
	 * {@code DELETE  /categorys/:id} : delete the "id" category.
	 *
	 * @param id the id of the category to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/categorys/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
		log.debug("REST request to delete Category : {}", id);
		categoryRepository.deleteById(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
	}
}
