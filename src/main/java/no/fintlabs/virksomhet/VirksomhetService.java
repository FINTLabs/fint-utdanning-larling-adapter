package no.fintlabs.virksomhet;

import lombok.SneakyThrows;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.Contract;
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
        restUtil.getRequestData()
                .subscribe(requestData ->
                        requestData.getKontrakter().forEach(contract -> {
                            VirksomhetResource virksomhetResource = createVirksomhetResource(contract);
                            virksomhetResources.add(virksomhetResource);
                        }));
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

        return virksomhetResource;
    }

}
