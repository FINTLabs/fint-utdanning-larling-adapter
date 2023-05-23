package no.fintlabs.restutil.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestData {

    private String errorMessage;
    private int antall;
    private List<Contract> kontrakter;

}
