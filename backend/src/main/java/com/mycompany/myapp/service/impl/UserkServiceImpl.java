package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.dao.Group;
import com.mycompany.myapp.dao.User;
import com.mycompany.myapp.domain.Tenant;
import com.mycompany.myapp.domain.Userk;
import com.mycompany.myapp.repository.UserkRepository;
import com.mycompany.myapp.service.UserkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Userk}.
 */
@Service
@Transactional
public class UserkServiceImpl implements UserkService {

    private final Logger log = LoggerFactory.getLogger(UserkServiceImpl.class);

    private final UserkRepository userkRepository;

    public UserkServiceImpl(UserkRepository userkRepository) {
        this.userkRepository = userkRepository;
    }

    @Override
    public Userk save(Userk userk) {
        log.debug("Request to save Userk : {}", userk);
        return userkRepository.save(userk);
    }

    @Override
    public Userk update(Userk userk) {
        log.debug("Request to save Userk : {}", userk);
        return userkRepository.save(userk);
    }

    @Override
    public Optional<Userk> partialUpdate(Userk userk) {
        log.debug("Request to partially update Userk : {}", userk);

        return userkRepository
            .findById(userk.getId())
            .map(existingUserk -> {
                if (userk.getUsername() != null) {
                    existingUserk.setUsername(userk.getUsername());
                }
                if (userk.getEmail() != null) {
                    existingUserk.setEmail(userk.getEmail());
                }
                if (userk.getFirstname() != null) {
                    existingUserk.setFirstname(userk.getFirstname());
                }
                if (userk.getLastname() != null) {
                    existingUserk.setLastname(userk.getLastname());
                }

                return existingUserk;
            })
            .map(userkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Userk> findAll(Pageable pageable) {
        log.debug("Request to get all Userks");
        return userkRepository.findAll(pageable);
    }

    public Page<Userk> findAllWithEagerRelationships(Pageable pageable) {
        return userkRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Userk> findOne(Long id) {
        log.debug("Request to get Userk : {}", id);
        return userkRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Userk : {}", id);
        userkRepository.deleteById(id);
    }

    @Override
    public Userk createUserkAndSaveUser(Userk userk) throws URISyntaxException {

        WebClient client = WebClient.create();
        User user = new User();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
//        user.setFirstname(user.getFirstname());
//        user.setLastname(user.getLastname());
//        user.setTenant(user.getTenant());

        Userk response = client.post()
            .uri(new URI("http://localhost:9080/auth/admin/realms/watchtower/users"))
            .header("Authorization", "Bearer  eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJnbUZacWlSdnBxdE9UanFNc1BIZUF1a21vLUR1Ty1oTGkyNEZzZmlqTjZFIn0.eyJleHAiOjE2NTcxMjkwNjksImlhdCI6MTY1NzEyODc2OSwianRpIjoiZDM3MDQ5M2MtMGZjMi00NDA3LWEyZTItMzYyYTA1YjA5ZDg0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL2F1dGgvcmVhbG1zL3dhdGNodG93ZXIiLCJzdWIiOiIyZmI2N2EwOC03ZGIzLTRmNTItYTVjZC1jYjEzYTllODhiNDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhZG1pbi1jbGkiLCJhY3IiOiIxIiwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiY2xpZW50SWQiOiJhZG1pbi1jbGkiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudEhvc3QiOiIxMjcuMC4wLjEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtYWRtaW4tY2xpIiwiY2xpZW50QWRkcmVzcyI6IjEyNy4wLjAuMSJ9.UNhZASFIolQYqOhUUOl1Ic7RanaZkK4HtoWK18Ihftj8HXPJVST3Cg-ByARNGEqSjXhe33P30gWH0h6IHBUX8F1jCHEE10-z3lXp39Vu9t1WOok18n3bQG302OCT5BZZwGvG7TIi_wf5qdOTy5Glq0x8L2nKzHcrqXizU5sv-0vsgPCJdaR76TJoaCYVMY8mLZKEQCGEIms749tuSPMIYHP7htJa4koq1pbDsroGYKwmw6os6MvcMQJBPXo6Ty4IyjGfBUMBPpRL0kas8-OYN7I6tXY5PeHYQYaLAYPBpLQTNTt3HOWcanux9NXkYXLORDGGpiv8AE9Sa6cTkucDag ")
            .body(Mono.just(user), User.class)
            .retrieve()
            .bodyToMono(Userk.class)
            .block();


        return userkRepository.save(userk);
    }
}
