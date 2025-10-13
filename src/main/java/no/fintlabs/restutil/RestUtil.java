package no.fintlabs.restutil;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
public class RestUtil {

    private final WebClient webClient;
    private final String orgNumber;
    private final String apiKey;

    public RestUtil(
            WebClient.Builder webClientBuilder,
            @Value("${fint.fylkesnr}") String orgNumber,
            @Value("${fint.api-key}") String apiKey,
            @Value("${fint.data-source-url}") String dataSourceUrl
    ) {
        this.orgNumber = orgNumber;
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl(dataSourceUrl)
                .codecs(this::configureCodecs)
                .build();
        log.info("RestUtil initialized with URL: {}", dataSourceUrl);
    }

    private void configureCodecs(ClientCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB buffer size
    }


    public RequestData getRequestData() {
        return webClient.get()
                .header("api-key", apiKey)
                .header("fylkesnr", orgNumber)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(RequestData.class)
                .block();
    }

}
