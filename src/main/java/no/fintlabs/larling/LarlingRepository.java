package no.fintlabs.larling;

import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.events.WriteableResourceRepository;
import no.fintlabs.adapter.models.RequestFintEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LarlingRepository implements WriteableResourceRepository<LarlingResource> {

    private final LarlingService larlingService;

    public LarlingRepository(LarlingService larlingService) {
        this.larlingService = larlingService;
    }

    @Override
    public LarlingResource saveResources(LarlingResource resource, RequestFintEvent requestFintEvent) {
        return null;
    }

    @Override
    public List<LarlingResource> getResources() {
        return larlingService.getLarlingResources();
    }

    @Override
    public List<LarlingResource> getUpdatedResources() {
        return new ArrayList<>();
    }
}