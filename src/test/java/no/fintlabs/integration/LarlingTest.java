package no.fintlabs.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.BaseIntegrationTest;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.larling.LarlingPublisher;
import no.fintlabs.restutil.model.Contract;
import no.fintlabs.restutil.model.RequestData;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// BaseTestConfiguration prevents AdapterRegisterService from running during tests
@SpringBootTest
@Import(BaseTestConfiguration.class)
public class LarlingTest extends BaseIntegrationTest {


    @MockitoSpyBean
    private LarlingPublisher larlingPublisher;

    @Captor
    ArgumentCaptor<SyncData<LarlingResource>> captor;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        doReturn(1).when(larlingPublisher).submit(any());
        larlingPublisher.doInitialSync();

        // Capture the values that is to be submitted
        verify(larlingPublisher).submit(captor.capture());
        SyncData<LarlingResource> submittedData = captor.getValue();

        // Verify that the submitted data contains values from contract.json
        List<LarlingResource> submittedResources = submittedData.getResources();
        LarlingResource resource = submittedResources.get(0);

        assertThat("sys-1").isEqualTo(resource.getSystemId().getIdentifikatorverdi());
        assertThat("Apprenticeship").isEqualTo(resource.getKontraktstype());
        assertThat(resource.getPerson().get(0).getHref()).endsWith("12345678901");
    }

}

