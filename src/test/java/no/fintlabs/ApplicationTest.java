package no.fintlabs;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8089)
class ApplicationTest {

    @Test
    void testStubbedContractEndpoint() {
        stubFor(get(urlEqualTo("/api/data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("contract.json")));

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:8089/api/data", String.class);

        System.out.println(response);
        assertThat(response).contains("Apprenticeship");
    }
}
