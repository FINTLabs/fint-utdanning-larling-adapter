package no.fintlabs.model.virksomhet;

import no.fint.model.resource.felles.VirksomhetResource;
import no.fintlabs.adapter.config.AdapterProperties;
import no.fintlabs.adapter.datasync.ResourceSubscriber;
import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.SyncPageEntry;
import no.fintlabs.adapter.validator.ValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VirksomhetSubscriber extends ResourceSubscriber<VirksomhetResource, VirksomhetPublisher> {

    protected VirksomhetSubscriber(WebClient webClient, AdapterProperties props, VirksomhetPublisher publisher, ValidatorService<VirksomhetResource> validatorService) {
        super(webClient, props, publisher, validatorService);
    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilities().get("virksomhet");
    }

    @Override
    protected SyncPageEntry<VirksomhetResource> createSyncPageEntry(VirksomhetResource resource) {
        return SyncPageEntry.of(resource.getVirksomhetsId().getIdentifikatorverdi(), resource);
    }
}
