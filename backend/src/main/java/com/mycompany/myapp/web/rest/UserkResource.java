package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Userk;
import com.mycompany.myapp.repository.UserkRepository;
import com.mycompany.myapp.service.UserkService;
import com.mycompany.myapp.service.impl.TenantServiceImpl;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Userk}.
 */
@RestController
@RequestMapping("/api")
public class UserkResource {

    private final Logger log = LoggerFactory.getLogger(UserkResource.class);

    private static final String ENTITY_NAME = "userk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserkService userkService;

    private final UserkRepository userkRepository;

    public UserkResource(UserkService userkService, UserkRepository userkRepository) {
        this.userkService = userkService;
        this.userkRepository = userkRepository;
    }

    /**
     * {@code POST  /userks} : Create a new userk.
     *
     * @param userk the userk to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userk, or with status {@code 400 (Bad Request)} if the userk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/userks")
    public ResponseEntity<Userk> createUserk(@Valid @RequestBody Userk userk) throws URISyntaxException {
        log.debug("REST request to save Userk : {}", userk);
        if (userk.getId() != null) {
            throw new BadRequestAlertException("A new userk cannot already have an ID", ENTITY_NAME, "idexists");
        }

        userkService.createUserkAndSaveUser(userk);
        Userk result = userkService.save(userk);
        return ResponseEntity
            .created(new URI("/api/userks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /userks/:id} : Updates an existing userk.
     *
     * @param id the id of the userk to save.
     * @param userk the userk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userk,
     * or with status {@code 400 (Bad Request)} if the userk is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/userks/{id}")
    public ResponseEntity<Userk> updateUserk(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Userk userk)
        throws URISyntaxException {
        log.debug("REST request to update Userk : {}, {}", id, userk);
        if (userk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Userk result = userkService.update(userk);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userk.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /userks/:id} : Partial updates given fields of an existing userk, field will ignore if it is null
     *
     * @param id the id of the userk to save.
     * @param userk the userk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userk,
     * or with status {@code 400 (Bad Request)} if the userk is not valid,
     * or with status {@code 404 (Not Found)} if the userk is not found,
     * or with status {@code 500 (Internal Server Error)} if the userk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/userks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Userk> partialUpdateUserk(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Userk userk
    ) throws URISyntaxException {
        log.debug("REST request to partial update Userk partially : {}, {}", id, userk);
        if (userk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Userk> result = userkService.partialUpdate(userk);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userk.getId().toString())
        );
    }

    /**
     * {@code GET  /userks} : get all the userks.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userks in body.
     */
    @GetMapping("/userks")
    public ResponseEntity<List<Userk>> getAllUserks(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Userks");
        Page<Userk> page;
        if (eagerload) {
            page = userkService.findAllWithEagerRelationships(pageable);
        } else {
            page = userkService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /userks/:id} : get the "id" userk.
     *
     * @param id the id of the userk to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userk, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/userks/{id}")
    public ResponseEntity<Userk> getUserk(@PathVariable Long id) {
        log.debug("REST request to get Userk : {}", id);
        Optional<Userk> userk = userkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userk);
    }

    /**
     * {@code DELETE  /userks/:id} : delete the "id" userk.
     *
     * @param id the id of the userk to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/userks/{id}")
    public ResponseEntity<Void> deleteUserk(@PathVariable Long id) {
        log.debug("REST request to delete Userk : {}", id);
        userkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
