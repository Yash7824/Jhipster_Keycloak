package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Userk;

import java.net.URISyntaxException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Userk}.
 */
public interface UserkService {
    /**
     * Save a userk.
     *
     * @param userk the entity to save.
     * @return the persisted entity.
     */
    Userk save(Userk userk);

    /**
     * Updates a userk.
     *
     * @param userk the entity to update.
     * @return the persisted entity.
     */
    Userk update(Userk userk);

    /**
     * Partially updates a userk.
     *
     * @param userk the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Userk> partialUpdate(Userk userk);

    /**
     * Get all the userks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Userk> findAll(Pageable pageable);

    /**
     * Get all the userks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Userk> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Userk> findOne(Long id);

    /**
     * Delete the "id" userk.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Userk createUserkAndSaveUser(Userk userk) throws URISyntaxException;
}
