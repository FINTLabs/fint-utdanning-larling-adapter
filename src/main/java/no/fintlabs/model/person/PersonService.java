package no.fintlabs.model.person;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.CacheService;
import no.fintlabs.TimeConverter;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final CacheService cacheService;
    private final TimeConverter timeConverter;

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
            personResource.setFodselsdato(timeConverter.convertToZuluDate(contract.getElev().getFodselsdato())); log.info(timeConverter.convertToZuluDate(contract.getElev().getFodselsdato()).toString());

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
        personResource.addSelf(Link.with(Person.class, "fodselsnummer", contract.getElev().getFodselsNummer()));

        return personResource;
    }

}
