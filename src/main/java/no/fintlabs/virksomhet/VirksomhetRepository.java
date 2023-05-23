package no.fintlabs.virksomhet;

import no.fint.model.resource.felles.VirksomhetResource;
import no.fintlabs.adapter.events.WriteableResourceRepository;
import no.fintlabs.adapter.models.RequestFintEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VirksomhetRepository implements WriteableResourceRepository<VirksomhetResource> {

    private final VirksomhetService virksomhetService;

    public VirksomhetRepository(VirksomhetService virksomhetService) {
        this.virksomhetService = virksomhetService;
    }

    @Override
    public VirksomhetResource saveResources(VirksomhetResource resource, RequestFintEvent requestFintEvent) {
        return null;
    }

    @Override
    public List<VirksomhetResource> getResources() {
        return virksomhetService.getVirksomhetResources();
    }

    @Override
    public List<VirksomhetResource> getUpdatedResources() {
        return new ArrayList<>();
    }
}