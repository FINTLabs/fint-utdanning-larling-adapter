package no.fintlabs.restutil.model;

import java.util.List;
import lombok.Data;

@Data
public class RequestData {

  private String errorMessage;
  private int antall;
  private List<Contract> kontrakter;
}
