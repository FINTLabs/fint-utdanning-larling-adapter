package no.fintlabs.model.larling;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fintlabs.CacheService;
import no.fintlabs.TimeConverter;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LarlingService {

    private final CacheService cacheService;
    private final TimeConverter timeConverter;

    public List<LarlingResource> getLarlingResources() {
        try {
            List<LarlingResource> larlingResources = cacheService.getContracts().stream()
                    .map(this::createLarlingResource)
                    .collect(Collectors.toList());

            cacheService.finishProcess();

            return larlingResources;
        } catch (Exception exception) {
            cacheService.handleProcessingError();
            throw exception;
        }
    }

    @SneakyThrows
    private LarlingResource createLarlingResource(Contract contract) {
        LarlingResource larlingResource = new LarlingResource();
        if (!contract.getType().isEmpty())
            larlingResource.setKontraktstype(contract.getType());

        Periode periode = new Periode();
        if (!contract.getStartDato().isEmpty())
            periode.setStart(timeConverter.convertToZuluDate(contract.getStartDato()));
        log.info(contract.getStartDato());
        if (!contract.getSluttDato().isEmpty())
            periode.setSlutt(timeConverter.convertToZuluDate(contract.getSluttDato()));
        larlingResource.setLaretid(periode);

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(contract.getElev().getSystemId());
        larlingResource.setSystemId(identifikator);

        larlingResource.addPerson(Link.with(PersonResource.class, "fodselsnummer", contract.getElev().getFodselsNummer()));
        larlingResource.addBedrift(Link.with(VirksomhetResource.class, "virksomhetsid", contract.getBedriftsNummer()));
        larlingResource.addProgramomrade(Link.with(ProgramomradeResource.class, "utdanning/utdanningsprogram/programomrade/systemid", setProgramKode(contract.getProgramKode())));
        larlingResource.addSelf(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));

        return larlingResource;
    }

    // This is a temporray fix until IST sends the correct programKode in the API
    private String setProgramKode(String programKode) {
        return programKode + "-".repeat(Math.max(0, 10 - programKode.length()));
    }

}
