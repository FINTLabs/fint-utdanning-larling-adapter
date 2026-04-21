package no.fintlabs.integration;

import no.novari.fint.model.resource.felles.PersonResource;
import no.fintlabs.BaseIntegrationTest;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.TimeConverter;
import no.fintlabs.adapter.datasync.SyncData;
import no.fintlabs.model.person.PersonPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(BaseTestConfiguration.class)
public class PersonTest extends BaseIntegrationTest {

    @Autowired
    private TimeConverter timeConverter;

    @MockitoSpyBean
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

        // Verify that the submitted data contains values from contract.json
        List<PersonResource> submittedResources = submittedData.getResources();
        PersonResource resource = submittedResources.get(0);

        assertThat("12345678901").isEqualTo(resource.getFodselsnummer().getIdentifikatorverdi());
        assertThat("Test").isEqualTo(resource.getNavn().getFornavn());
        assertThat("Student").isEqualTo(resource.getNavn().getEtternavn());
        assertThat(timeConverter.convertToZuluDate("01.01.2000")).isEqualTo(resource.getFodselsdato());
        assertThat("test@student.no").isEqualTo(resource.getKontaktinformasjon().getEpostadresse());
        assertThat("12345678").isEqualTo(resource.getKontaktinformasjon().getMobiltelefonnummer());
    }
}
