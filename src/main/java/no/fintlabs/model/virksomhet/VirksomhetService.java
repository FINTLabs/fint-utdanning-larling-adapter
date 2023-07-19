package no.fintlabs.model.virksomhet;

import lombok.SneakyThrows;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.CacheService;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirksomhetService {

    private final CacheService cacheService;

    public VirksomhetService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public List<VirksomhetResource> getVirksomhetResources() {
        try {

            List<VirksomhetResource> virksomhetResources = cacheService.getContracts().stream()
                    .map(this::createVirksomhetResource)
                    .collect(Collectors.toList());

            cacheService.finishProcess();

            return virksomhetResources;
        } catch (Exception exception) {
            cacheService.handleProcessingError();
            throw exception;
        }
    }

    private void addLarlingLinks(VirksomhetResource virksomhetResource, String bedriftsNummer) {
        cacheService.getContracts(bedriftsNummer).forEach(contract ->
                virksomhetResource.addLarling(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()))
        );

    }

    @SneakyThrows
    private VirksomhetResource createVirksomhetResource(Contract contract) {
        VirksomhetResource virksomhetResource = new VirksomhetResource();
        String bedriftsNummer = contract.getBedriftsNummer();

        if (!contract.getBedriftsNavn().isEmpty())
            virksomhetResource.setOrganisasjonsnavn(contract.getBedriftsNavn());

        Identifikator bedriftIdentifikator = new Identifikator();
        bedriftIdentifikator.setIdentifikatorverdi(contract.getBedriftsNummer());
        virksomhetResource.setVirksomhetsId(bedriftIdentifikator);

        addLarlingLinks(virksomhetResource, bedriftsNummer);
        virksomhetResource.addSelf(Link.with(VirksomhetResource.class, "virksomhetsid", contract.getBedriftsNummer()));

        return virksomhetResource;
    }

}
