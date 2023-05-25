package no.fintlabs.model.larling;

import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.config.AdapterProperties;
import no.fintlabs.adapter.datasync.ResourceSubscriber;
import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.SyncPageEntry;
import no.fintlabs.adapter.validator.ValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LarlingSubscriber extends ResourceSubscriber<LarlingResource, LarlingPublisher> {

    protected LarlingSubscriber(WebClient webClient, AdapterProperties props, LarlingPublisher publisher, ValidatorService<LarlingResource> validatorService) {
        super(webClient, props, publisher, validatorService);
    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilities().get("larling");
    }

    @Override
    protected SyncPageEntry<LarlingResource> createSyncPageEntry(LarlingResource resource) {
        return SyncPageEntry.of(resource.getSystemId().getIdentifikatorverdi(), resource);
    }
}
