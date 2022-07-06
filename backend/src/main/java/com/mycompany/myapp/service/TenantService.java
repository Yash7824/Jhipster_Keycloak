package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Tenant;

import java.net.URISyntaxException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tenant}.
 */
public interface TenantService {
    /**
     * Save a tenant.
     *
     * @param tenant the entity to save.
     * @return the persisted entity.
     */
    Tenant save(Tenant tenant);

    /**
     * Updates a tenant.
     *
     * @param tenant the entity to update.
     * @return the persisted entity.
     */
    Tenant update(Tenant tenant);

    /**
     * Partially updates a tenant.
     *
     * @param tenant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tenant> partialUpdate(Tenant tenant);

    /**
     * Get all the tenants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tenant> findAll(Pageable pageable);

    /**
     * Get the "id" tenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tenant> findOne(Long id);

    /**
     * Delete the "id" tenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Tenant CreateKGroupAndSaveTenant(Tenant tenant) throws URISyntaxException;
}
