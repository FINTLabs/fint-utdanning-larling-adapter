package no.fintlabs.larling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.events.WriteableResourceRepository;
import no.fintlabs.adapter.models.RequestFintEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class LarlingRepository implements WriteableResourceRepository<LarlingResource> {

    private final LarlingJpaRepository larlingJpaRepository;

    public LarlingRepository(LarlingJpaRepository larlingJpaRepository) {
        this.larlingJpaRepository = larlingJpaRepository;
    }

    @Override
    public LarlingResource saveResources(LarlingResource resource, RequestFintEvent requestFintEvent) {
        LarlingEntity entity = LarlingEntity.toEntity(resource, requestFintEvent.getOrgId());
        return larlingJpaRepository.save(entity).getResource();
    }

    @Override
    public List<LarlingResource> getResources() {
        return larlingJpaRepository.findAllResources();
    }

    @Override
    public List<LarlingResource> getUpdatedResources() {
        return new ArrayList<>();
    }
}