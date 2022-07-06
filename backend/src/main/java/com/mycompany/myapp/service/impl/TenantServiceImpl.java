package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.dao.Group;
import com.mycompany.myapp.domain.Tenant;
import com.mycompany.myapp.repository.TenantRepository;
import com.mycompany.myapp.service.TenantService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Tenant}.
 */
@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }



    @Override
    public Tenant CreateKGroupAndSaveTenant(Tenant tenant) throws URISyntaxException {
        // api call to keycloak for creating group [tenant.getName] restTemplate, keycloak admin credentials, make use of WebClient


        WebClient client = WebClient.create();

        Group group = new Group();
        group.setName(tenant.getName());
        System.out.println(tenant.getName());

        /*
        WebClient.ResponseSpec responseSpec = client.get()
            .uri("watchtower/groups")
            .retrieve();

         */


        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add("key", "value");

        Tenant response = client.post()
            .uri(new URI("http://localhost:9080/auth/admin/realms/watchtower/groups"))
            .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnbUZacWlSdnBxdE9UanFNc1BIZUF1a21vLUR1Ty1oTGkyNEZzZmlqTjZFIn0.eyJleHAiOjE2NTcxMTI4MjgsImlhdCI6MTY1NzExMjUyOCwianRpIjoiNjQ3M2FmMTUtMDIxMS00YTU5LThiM2UtNzc0NTFiZDMzNjlhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL3dhdGNodG93ZXIiLCJzdWIiOiIyZmI2N2EwOC03ZGIzLTRmNTItYTVjZC1jYjEzYTllODhiNDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jbGkiLCJhY3IiOiIxIiwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiY2xpZW50SWQiOiJhZG1pbi1jbGkiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudEhvc3QiOiIxMjcuMC4wLjEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtYWRtaW4tY2xpIiwiY2xpZW50QWRkcmVzcyI6IjEyNy4wLjAuMSJ9.Jmj08VswbkUIffaO3-mYT7MRuxxHH6qi1NOTjo0b8kmzuJfHqaThUtRKU3PZ5Ld0exSOkzg9e4KK0013zEWveb-IoNkty5WRvqygnnunIviIRtSWS6jiYKvWkq_qkxyGuem46GdLtq0rPyFWHKY89hUXUbYD4Hhdd4h-i4lI7P6vDb-_9F93Rvne20EDI4zHjCO_QK_jVBVImKiJ8919f80XiiiLniviI6mdFOja6qY1c-UCF8sh75rT2XI84uF3FFcT4YrwMrPlbw-wF01ELEaHwjgf372wpV_mT8klB0bPfKkAZcBGHCeK2ck9VppqU3631_50qAEkL06N1ru6Bw  ")
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .accept(MediaType.APPLICATION_JSON)
//            .accept(MediaType.ALL)
            .body(Mono.just(group),Group.class)
            .retrieve()
            .bodyToMono(Tenant.class)
            .block();


        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant save(Tenant tenant) {
        log.debug("Request to save Tenant : {}", tenant);
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant update(Tenant tenant) {
        log.debug("Request to save Tenant : {}", tenant);
        return tenantRepository.save(tenant);
    }

    @Override
    public Optional<Tenant> partialUpdate(Tenant tenant) {
        log.debug("Request to partially update Tenant : {}", tenant);

        return tenantRepository
            .findById(tenant.getId())
            .map(existingTenant -> {
                if (tenant.getName() != null) {
                    existingTenant.setName(tenant.getName());
                }

                return existingTenant;
            })
            .map(tenantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tenant> findAll(Pageable pageable) {
        log.debug("Request to get all Tenants");
        return tenantRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tenant> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tenant : {}", id);
        tenantRepository.deleteById(id);
    }
}
