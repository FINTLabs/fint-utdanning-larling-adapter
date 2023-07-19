package no.fintlabs.model.person;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.CacheService;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonService {

    private final CacheService cacheService;
    private final SimpleDateFormat dateFormat;

    public PersonService(CacheService cacheService) {
        this.cacheService = cacheService;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public List<PersonResource> getPersonResources() {
        try {
            List<PersonResource> personResources = cacheService.getContracts().stream()
                    .map(this::createPersonResource)
                    .collect(Collectors.toList());

            cacheService.finishProcess();

            return personResources;
        } catch (Exception exception) {
            cacheService.handleProcessingError();
            throw exception;
        }
    }

    @SneakyThrows
    private PersonResource createPersonResource(Contract contract) {
        PersonResource personResource = new PersonResource();
        if (!contract.getElev().getFodselsdato().isEmpty())
            personResource.setFodselsdato(dateFormat.parse(contract.getElev().getFodselsdato()));

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(contract.getElev().getFodselsNummer());
        personResource.setFodselsnummer(identifikator);

        Personnavn personnavn = new Personnavn();
        personnavn.setFornavn(contract.getElev().getFornavn());
        personnavn.setEtternavn(contract.getElev().getEtternavn());
        personResource.setNavn(personnavn);

        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        if (!contract.getElev().getEpost().isEmpty())
            kontaktinformasjon.setEpostadresse(contract.getElev().getEpost());
        if (!contract.getElev().getMobilNummer().isEmpty())
            kontaktinformasjon.setMobiltelefonnummer(contract.getElev().getMobilNummer());
        personResource.setKontaktinformasjon(kontaktinformasjon);

        personResource.addLarling(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));
        personResource.addSelf(Link.with(PersonResource.class, "utdanning/larling/person/fodselsnummer", contract.getElev().getFodselsNummer()));

        return personResource;
    }

}
