package no.fintlabs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.novari.fint.model.felles.kompleksedatatyper.Periode;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(TimeConverter.class)
class DateFormatTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TimeConverter timeConverter;

    @Test
    void fodselsdatoIsWrittenAsUtcIso8601() throws Exception {
        PersonResource person = new PersonResource();
        person.setFodselsdato(timeConverter.convertToZuluDate("11.02.2007"));

        JsonNode json = objectMapper.valueToTree(person);

        assertThat(json.get("fodselsdato").asText()).isEqualTo("2007-02-11T12:00:00Z");
    }

    @Test
    void laretidIsWrittenAsUtcIso8601() throws Exception {
        Periode periode = new Periode();
        periode.setStart(timeConverter.convertToZuluDate("17.08.2027"));
        periode.setSlutt(timeConverter.convertToZuluDate("09.08.2028"));
        LarlingResource larling = new LarlingResource();
        larling.setLaretid(periode);

        JsonNode json = objectMapper.valueToTree(larling);

        assertThat(json.get("laretid").get("start").asText()).isEqualTo("2027-08-17T12:00:00Z");
        assertThat(json.get("laretid").get("slutt").asText()).isEqualTo("2028-08-09T12:00:00Z");
    }

}
