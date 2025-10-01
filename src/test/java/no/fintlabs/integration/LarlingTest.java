package no.fintlabs.integration;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.adapter.AdapterRegisterService;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.larling.LarlingPublisher;
import no.fintlabs.model.larling.LarlingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.mockito.Mockito.*;

// BaseTestConfiguration prevents AdapterRegisterService from running during tests
@SpringBootTest
@Import(BaseTestConfiguration.class)
public class LarlingTest {

    @MockBean
    private LarlingRepository larlingRepository;

    @SpyBean
    private LarlingPublisher larlingPublisher;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        // Arrange
        List<LarlingResource> testResources = List.of(
                createLarlingResource("1"),
                createLarlingResource("2"),
                createLarlingResource("3")
        );

        when(larlingRepository.getResources()).thenReturn(testResources);

        // Act
        larlingPublisher.doFullSync();

        // Assert
        verify(larlingPublisher).submit(SyncData.ofPostData(testResources));
    }

    private LarlingResource createLarlingResource(String systemIdValue) {
        LarlingResource resource = new LarlingResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi(systemIdValue);
        resource.setSystemId(systemId);
        return resource;
    }

}

