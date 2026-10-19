package no.fintlabs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.novari.fint.model.felles.kompleksedatatyper.Periode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class JacksonDateFormatTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final TimeConverter timeConverter = new TimeConverter();

    @Test
    void shouldSerializeDatesAsIso8601Utc() throws Exception {
        Periode periode = new Periode();
        periode.setStart(timeConverter.convertToZuluDate("01.08.2024"));
        periode.setSlutt(Date.from(Instant.parse("2026-06-30T23:59:59.999Z")));

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(periode));

        assertThat(json.get("start").asText()).isEqualTo("2024-08-01T12:00:00Z");
        assertThat(json.get("slutt").asText()).isEqualTo("2026-06-30T23:59:59Z");
    }
}
