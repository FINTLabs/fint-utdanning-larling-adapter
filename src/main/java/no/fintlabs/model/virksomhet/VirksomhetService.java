package no.fintlabs.model.virksomhet;

import lombok.SneakyThrows;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
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

    public VirksomhetService(RestUtil restUtil) {
        this.restUtil = restUtil;
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
        if (!contract.getBedriftsNavn().isEmpty())
            virksomhetResource.setOrganisasjonsnavn(contract.getBedriftsNavn());

        Identifikator bedriftIdentifikator = new Identifikator();
        bedriftIdentifikator.setIdentifikatorverdi(contract.getBedriftsNummer());
        virksomhetResource.setVirksomhetsId(bedriftIdentifikator);

        virksomhetResource.addLink("larling", Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));
        virksomhetResource.addSelf(Link.with(VirksomhetResource.class, "systemid", contract.getBedriftsNummer()));

        return virksomhetResource;
    }

}
