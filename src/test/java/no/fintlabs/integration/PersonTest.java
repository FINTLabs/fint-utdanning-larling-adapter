package no.fintlabs.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.fint.model.resource.felles.PersonResource;
import no.fintlabs.BaseIntegrationTest;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.person.PersonPublisher;
import no.fintlabs.model.person.PersonSubscriber;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WireMockTest
@Import(BaseTestConfiguration.class)
public class PersonTest extends BaseIntegrationTest {

    @Mock
    private PersonSubscriber personSubscriber;

    @SpyBean
    private PersonPublisher personPublisher;

    @Captor
    ArgumentCaptor<SyncData<PersonResource>> captor;

    @Test
    void doFullSync_shouldSubmitAllResources() {
        doReturn(1).when(personPublisher).submit(any());
        personPublisher.doInitialSync();

        // Capture the values that is to be submitted
        verify(personPublisher).submit(captor.capture());
        SyncData<PersonResource> submittedData = captor.getValue();

        // Verify that the submitted data contains values from person_contract.json
        List<PersonResource> submittedResources = submittedData.getResources();
        assertThat(submittedResources).isNotEmpty();
        assertThat(submittedResources)
                .anyMatch(resource -> "12345678901".equals(resource.getFodselsnummer().getIdentifikatorverdi()));

    }

}
