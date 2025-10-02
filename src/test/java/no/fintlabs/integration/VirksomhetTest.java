package no.fintlabs.integration;

import no.fint.model.resource.felles.VirksomhetResource;
import no.fintlabs.BaseIntegrationTest;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.virksomhet.VirksomhetPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(BaseTestConfiguration.class)
public class VirksomhetTest extends BaseIntegrationTest {

    @SpyBean
    private VirksomhetPublisher virksomhetPublisher;

    @Captor
    private ArgumentCaptor<SyncData<VirksomhetResource>> captor;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        doReturn(1).when(virksomhetPublisher).submit(any());
        virksomhetPublisher.doInitialSync();

        // Capture the values that is to be submitted
        verify(virksomhetPublisher).submit(captor.capture());
        SyncData<VirksomhetResource> submittedData = captor.getValue();

        // Verify that the submitted data contains values from contract.json
        List<VirksomhetResource> submittedResources = submittedData.getResources();
        assertThat(submittedResources).isNotEmpty();
        assertThat(submittedResources)
                .anyMatch(resource -> "987654321".equals(resource.getVirksomhetsId().getIdentifikatorverdi()));

    }

}
