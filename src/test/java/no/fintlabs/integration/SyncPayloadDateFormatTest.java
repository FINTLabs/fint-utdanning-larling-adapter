package no.fintlabs.integration;

import com.jayway.jsonpath.JsonPath;
import no.fintlabs.BaseIntegrationTest;
import no.fintlabs.BaseTestConfiguration;
import no.fintlabs.model.larling.LarlingPublisher;
import no.fintlabs.model.person.PersonPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

// Sends a real sync through the adapter's WebClient and checks the body that reaches the provider,
// so this fails if the WebClient stops using the Spring-configured ObjectMapper.
@SpringBootTest
@Import(BaseTestConfiguration.class)
public class SyncPayloadDateFormatTest extends BaseIntegrationTest {

    // contract.json has 01.01.2000 for every date, which TimeConverter sets to 12:00 UTC
    private static final String EXPECTED_DATE = "2000-01-01T12:00:00Z";

    @Autowired
    private PersonPublisher personPublisher;

    @Autowired
    private LarlingPublisher larlingPublisher;

    @Test
    void personSync_shouldSendFodselsdatoAsIso8601() {
        personPublisher.doFullSync();

        String body = awaitPostBody("/provider/utdanning/larling/person");

        Object fodselsdato = JsonPath.read(body, "$.resources[0].resource.fodselsdato");
        assertThat(fodselsdato).isEqualTo(EXPECTED_DATE);
    }

    @Test
    void larlingSync_shouldSendLaretidAsIso8601() {
        larlingPublisher.doFullSync();

        String body = awaitPostBody("/provider/utdanning/larling/larling");

        Object start = JsonPath.read(body, "$.resources[0].resource.laretid.start");
        Object slutt = JsonPath.read(body, "$.resources[0].resource.laretid.slutt");
        assertThat(start).isEqualTo(EXPECTED_DATE);
        assertThat(slutt).isEqualTo(EXPECTED_DATE);
    }

    private String awaitPostBody(String url) {
        return await().atMost(Duration.ofSeconds(10))
                .until(() -> wireMockServer.findAll(postRequestedFor(urlEqualTo(url))), requests -> !requests.isEmpty())
                .get(0)
                .getBodyAsString();
    }
}
