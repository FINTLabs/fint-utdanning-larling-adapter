package no.fintlabs.larling;

import no.fint.model.resource.utdanning.larling.LarlingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LarlingJpaRepository extends JpaRepository<LarlingEntity, String> {

    @Query("SELECT l.resource FROM larling l")
    List<LarlingResource> findAllResources();

}

