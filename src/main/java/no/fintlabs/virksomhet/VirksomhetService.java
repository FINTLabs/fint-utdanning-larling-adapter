package no.fintlabs.virksomhet;

import lombok.SneakyThrows;
import no.fint.model.felles.Virksomhet;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.utdanning.larling.Larling;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.Contract;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class VirksomhetService {

    private final RestUtil restUtil;
    private final SimpleDateFormat dateFormat;

    public VirksomhetService(RestUtil restUtil) {
        this.restUtil = restUtil;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public List<VirksomhetResource> getVirksomhetResources() {
        List<VirksomhetResource> virksomhetResources = new ArrayList<>();
        RequestData requestData = restUtil.getRequestData().block();

        if (requestData != null) {
            requestData.getKontrakter().forEach(contract -> {
                VirksomhetResource virksomhetResource = createVirksomhetResource(contract);
                virksomhetResources.add(virksomhetResource);
            });
        }

        return virksomhetResources;
    }

    @SneakyThrows
    private VirksomhetResource createVirksomhetResource(Contract contract) {
        VirksomhetResource virksomhetResource = new VirksomhetResource();
        virksomhetResource.setOrganisasjonsnavn(contract.getBedriftsNavn());

        Identifikator bedriftIdentifikator = new Identifikator();
        Periode periode = new Periode();

        periode.setStart(dateFormat.parse(contract.getStartDato()));
        periode.setSlutt(dateFormat.parse(contract.getSluttDato()));
        bedriftIdentifikator.setGyldighetsperiode(periode);

        bedriftIdentifikator.setIdentifikatorverdi(contract.getBedriftsNummer());
        virksomhetResource.setVirksomhetsId(bedriftIdentifikator);

        virksomhetResource.addLink("larling", Link.with(Larling.class, "systemid", contract.getElev().getSystemId()));
        virksomhetResource.addSelf(Link.with(Virksomhet.class, "bedriftsnummer", contract.getBedriftsNummer()));

        return virksomhetResource;
    }

}
