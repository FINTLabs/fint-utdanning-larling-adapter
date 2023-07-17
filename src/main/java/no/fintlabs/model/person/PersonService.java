package no.fintlabs.model.person;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
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
public class PersonService {

    private final RestUtil restUtil;
    private final SimpleDateFormat dateFormat;

    public PersonService(RestUtil restUtil) {
        this.restUtil = restUtil;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public List<PersonResource> getPersonResources() {
        List<PersonResource> personResources = new ArrayList<>();
        RequestData requestData = restUtil.getRequestData().block();

        if (requestData != null) {
            requestData.getKontrakter().forEach(contract -> {
                PersonResource personResource = createPersonResource(contract);
                personResources.add(personResource);
            });
        }

        return personResources;
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
        personnavn.setEtternavn(contract.getElev().getFornavn());
        personnavn.setEtternavn(contract.getElev().getEtternavn());
        personResource.setNavn(personnavn);

        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        if (!contract.getElev().getEpost().isEmpty())
            kontaktinformasjon.setEpostadresse(contract.getElev().getEpost());
        if (!contract.getElev().getMobilNummer().isEmpty())
            kontaktinformasjon.setMobiltelefonnummer(contract.getElev().getMobilNummer());
        personResource.setKontaktinformasjon(kontaktinformasjon);

        personResource.addLarling(Link.with(LarlingResource.class, "systemid", contract.getElev().getSystemId()));
        personResource.addSelf(Link.with(Person.class, "utdanning/larling/person/fodselsnummer", contract.getElev().getFodselsNummer()));

        return personResource;
    }

}
