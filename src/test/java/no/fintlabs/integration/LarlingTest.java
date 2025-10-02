package no.fintlabs.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.larling.LarlingPublisher;
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
import static org.mockito.Mockito.*;

// BaseTestConfiguration prevents AdapterRegisterService from running during tests
@SpringBootTest
@WireMockTest(httpPort = 8089)
@Import(BaseTestConfiguration.class)
public class LarlingTest {

    @SpyBean
    private LarlingPublisher larlingPublisher;

    @Captor
    ArgumentCaptor<SyncData<LarlingResource>> captor;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        stubFor(get(urlEqualTo("/rest/laktiv"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("contract.json")));

        larlingPublisher.doInitialSync();

        // Capture the values that is to be submitted
        verify(larlingPublisher).submit(captor.capture());
        SyncData<LarlingResource> submittedData = captor.getValue();

        // Verify that the submitted data contains values from contract.json
        List<LarlingResource> submittedResources = submittedData.getResources();
        assertThat(submittedResources).isNotEmpty();
        assertThat(submittedResources)
                .anyMatch(resource -> "sys-1".equals(resource.getSystemId().getIdentifikatorverdi()));
    }


}

