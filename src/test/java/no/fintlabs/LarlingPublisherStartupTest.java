package no.fintlabs;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.fintlabs.model.larling.LarlingPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@WireMockTest(httpPort = 8089)
@SpringBootTest
class LarlingPublisherStartupTest {


    @SpyBean
    private LarlingPublisher larlingPublisher;

    @Test
    void doFullSync() {
        stubFor(get(urlEqualTo("/rest/laktiv"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("contract.json")));

        larlingPublisher.doFullSync();
        // TODO: assert to ensure that doFullSync has run
    }
}
