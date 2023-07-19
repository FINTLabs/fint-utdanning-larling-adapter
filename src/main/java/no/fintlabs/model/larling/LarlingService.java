package no.fintlabs.model.larling;

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
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LarlingService {

    private final CacheService cacheService;
    private final SimpleDateFormat dateFormat;

    public LarlingService(CacheService cacheService) {
        this.cacheService = cacheService;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

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
            periode.setStart(dateFormat.parse(contract.getStartDato()));
        if (!contract.getSluttDato().isEmpty())
            periode.setSlutt(dateFormat.parse(contract.getSluttDato()));
        larlingResource.setLaretid(periode);

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(contract.getElev().getSystemId());
        larlingResource.setSystemId(identifikator);

        larlingResource.addPerson(Link.with(PersonResource.class, "utdanning/larling/person/fodselsnummer", contract.getElev().getFodselsNummer()));
        larlingResource.addBedrift(Link.with(VirksomhetResource.class, "utdanning/larling/virksomhet/virksomhetsid", contract.getBedriftsNummer()));
        larlingResource.addProgramomrade(Link.with(ProgramomradeResource.class, "utdanning/utdanningsprogram/programomrade/systemid", contract.getProgramKode()));
        larlingResource.addSelf(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));

        return larlingResource;
    }

}
