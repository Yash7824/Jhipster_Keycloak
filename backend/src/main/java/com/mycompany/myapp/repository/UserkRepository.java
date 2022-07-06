package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Userk;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Userk entity.
 */
@Repository
public interface UserkRepository extends JpaRepository<Userk, Long> {
    default Optional<Userk> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Userk> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Userk> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct userk from Userk userk left join fetch userk.tenant",
        countQuery = "select count(distinct userk) from Userk userk"
    )
    Page<Userk> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct userk from Userk userk left join fetch userk.tenant")
    List<Userk> findAllWithToOneRelationships();

    @Query("select userk from Userk userk left join fetch userk.tenant where userk.id =:id")
    Optional<Userk> findOneWithToOneRelationships(@Param("id") Long id);
}
