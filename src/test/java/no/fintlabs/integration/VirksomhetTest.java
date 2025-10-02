package no.fintlabs.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.fint.model.resource.felles.VirksomhetResource;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@WireMockTest(httpPort = 8089)
@Import(BaseTestConfiguration.class)
public class VirksomhetTest {

    @SpyBean
    private VirksomhetPublisher virksomhetPublisher;

    @Captor
    private ArgumentCaptor<SyncData<VirksomhetResource>> captor;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        stubFor(get(urlEqualTo("/rest/laktiv"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("contract.json")));

        virksomhetPublisher.doInitialSync();

        // Capture the values that is to be submitted
        verify(virksomhetPublisher).submit(captor.capture());
        SyncData<VirksomhetResource> submittedData = captor.getValue();

        // Verify that the submitted data contains values from person_contract.json
        List<VirksomhetResource> submittedResources = submittedData.getResources();
        assertThat(submittedResources).isNotEmpty();
        assertThat(submittedResources)
                .anyMatch(resource -> "987654321".equals(resource.getVirksomhetsId().getIdentifikatorverdi()));

    }

}
