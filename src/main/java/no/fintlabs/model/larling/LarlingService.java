package no.fintlabs.model.larling;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.Contract;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LarlingService {

    private final RestUtil restUtil;
    private final SimpleDateFormat dateFormat;

    public LarlingService(RestUtil restUtil) {
        this.restUtil = restUtil;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public List<LarlingResource> getLarlingResources() {
        List<LarlingResource> larlingResources = new ArrayList<>();
        RequestData requestData = restUtil.getRequestData().block();

        if (requestData != null) {
            requestData.getKontrakter().forEach(contract -> {
                LarlingResource larlingResource = createLarlingResource(contract);
                larlingResources.add(larlingResource);
            });
        }

        return larlingResources;
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

        larlingResource.addLink("person", Link.with(PersonResource.class, "fodselsnummer", contract.getElev().getFodselsNummer()));
        larlingResource.addLink("virksomhet", Link.with(VirksomhetResource.class, "systemid", contract.getBedriftsNummer()));
        // Add Programområde link
        larlingResource.addSelf(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));

        return larlingResource;
    }

}
