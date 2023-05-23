package no.fintlabs.restutil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Contract {

    @JsonProperty("elev")
    private Student elev;

    @JsonProperty("type")
    private String type;

    @JsonProperty("promrkode")
    private String programKode;

    @JsonProperty("promrnavn")
    private String programNavn;

    @JsonProperty("bnr")
    private String bedriftsNummer;

    @JsonProperty("bedrnavn")
    private String bedriftsNavn;

    @JsonProperty("start")
    private String startDato;

    @JsonProperty("slutt")
    private String sluttDato;

}
