package no.fintlabs;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import no.fintlabs.model.larling.LarlingPublisher;
import no.fintlabs.restutil.RestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@WireMockTest(httpPort = 8089)
@SpringBootTest
class LarlingPublisherStartupTest {


    @SpyBean
    private LarlingPublisher larlingPublisher;

    @Test
    void doFullSync_isCalledOnStartup() {
        stubFor(get(urlEqualTo("/rest/laktiv"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("contract.json")));

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:8089/rest/laktiv", String.class);
        System.out.println(response);

        verify(larlingPublisher, timeout(2000).times(1)).doFullSync();
    }
}
