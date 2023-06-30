package no.fintlabs.model.larling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.config.AdapterProperties;
import no.fintlabs.adapter.datasync.ResourcePublisher;
import no.fintlabs.adapter.datasync.ResourceRepository;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class LarlingPublisher extends ResourcePublisher<LarlingResource, ResourceRepository<LarlingResource>> {

    public LarlingPublisher(LarlingRepository repository, AdapterProperties adapterProperties) {
        super(repository, adapterProperties);
    }

    @Override
    @Scheduled(initialDelayString = "1000", fixedRateString = "7200000")
    public void doFullSync() {
        log.info("Start full sync for resource {}", getCapability().getEntityUri());
        submit(SyncData.ofPostData(repository.getResources()));
    }

    // Not in use
    @Override
    public void doDeltaSync() {
        submit(SyncData.ofPatchData(repository.getUpdatedResources()));
    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilityByResource("larling");
    }
}
