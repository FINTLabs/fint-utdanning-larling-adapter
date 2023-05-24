package no.fintlabs.restutil;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class RestUtil {

    private final WebClient webClient;

    @Value("${fint.fylkesnr}")
    private String orgNumber;

    @Value("${fint.api-key}")
    private String apiKey;

    public RestUtil(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.vigo.no/vigows/rest/laktiv")
                .codecs(this::configureCodecs)
                .build();
    }

    private void configureCodecs(ClientCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB buffer size
    }

    public Mono<RequestData> getRequestData() {
        return webClient.get()
                .header("api-key", apiKey)
                .header("fylkesnr", orgNumber)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(RequestData.class);
    }

}
