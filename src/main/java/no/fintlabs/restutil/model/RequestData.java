package no.fintlabs.restutil.model;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
public class RequestData {

    private String errorMessage;
    private int antall;
    private List<Contract> kontrakter;


    public List<Contract> getKontrakter() {
        System.out.println(kontrakter);
        return kontrakter;
    }

}
