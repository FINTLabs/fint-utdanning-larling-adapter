package no.fintlabs.larling;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.Person;
import no.fint.model.felles.Virksomhet;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fint.model.utdanning.larling.Larling;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        restUtil.getRequestData()
                .subscribe(requestData ->
                        requestData.getKontrakter().forEach(contract -> {
                            LarlingResource larlingResource = createLarlingResource(contract);
                            larlingResources.add(larlingResource);
                        }));
        return larlingResources;
    }

    @SneakyThrows
    private LarlingResource createLarlingResource(Contract contract) {
        LarlingResource larlingResource = new LarlingResource();
        larlingResource.setKontraktstype(contract.getType());

        Periode periode = new Periode();
        periode.setStart(dateFormat.parse(contract.getStartDato()));
        periode.setSlutt(dateFormat.parse(contract.getSluttDato()));
        larlingResource.setLaretid(periode);

        Identifikator identifikator = new Identifikator();
        identifikator.setGyldighetsperiode(periode);
        identifikator.setIdentifikatorverdi(contract.getElev().getSystemId());
        larlingResource.setSystemId(identifikator);

        larlingResource.addLink("person", Link.with(Person.class, "fodselsnummer", contract.getElev().getFodselsNummer()));
        larlingResource.addLink("virksomhet", Link.with(Virksomhet.class, "bedriftsnummer", contract.getBedriftsNummer()));
        larlingResource.addSelf(Link.with(Larling.class, "systemid", contract.getElev().getSystemId()));

        return larlingResource;
    }

}
