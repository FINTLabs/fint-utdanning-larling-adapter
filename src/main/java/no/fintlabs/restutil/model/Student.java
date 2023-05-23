package no.fintlabs.restutil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student {

    @JsonProperty("fornavn")
    private String fornavn;

    @JsonProperty("etternavn")
    private String etternavn;

    @JsonProperty("fdato")
    private String fodselsdato;

    @JsonProperty("fnr")
    private String fodselsNummer;

    @JsonProperty("sysId")
    private String systemId;

    @JsonProperty("epost")
    private String epost;

    @JsonProperty("mobilnr")
    private String mobilNummer;

}
