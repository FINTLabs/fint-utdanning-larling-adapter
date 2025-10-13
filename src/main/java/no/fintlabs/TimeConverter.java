package no.fintlabs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TimeConverter {

  private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public Date convertToZuluDate(String date) {
    LocalDate localDate = LocalDate.parse(date, inputFormatter);
    LocalDateTime dateTime = localDate.atTime(12, 0);
    return Date.from(dateTime.toInstant(ZoneOffset.UTC));
  }
}
