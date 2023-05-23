package no.fintlabs.person;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.Person;
import no.fint.model.felles.Virksomhet;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.utdanning.larling.Larling;
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
        personResource.setFodselsdato(dateFormat.parse(contract.getElev().getFodselsdato()));

        Identifikator identifikator = new Identifikator();
        Periode periode = new Periode();
        periode.setStart(dateFormat.parse(contract.getStartDato()));
        periode.setSlutt(dateFormat.parse(contract.getSluttDato()));
        identifikator.setGyldighetsperiode(periode);
        identifikator.setIdentifikatorverdi(contract.getElev().getFodselsNummer());
        personResource.setFodselsnummer(identifikator);

        Personnavn personnavn = new Personnavn();
        personnavn.setEtternavn(contract.getElev().getFornavn());
        personnavn.setEtternavn(contract.getElev().getEtternavn());
        personResource.setNavn(personnavn);

        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setEpostadresse(contract.getElev().getEpost());
        kontaktinformasjon.setMobiltelefonnummer(contract.getElev().getMobilNummer());
        personResource.setKontaktinformasjon(kontaktinformasjon);

        personResource.addLink("person", Link.with(Larling.class, "systemid", contract.getElev().getSystemId()));
        personResource.addLink("virksomhet", Link.with(Virksomhet.class, "systemid", contract.getBedriftsNummer()));
        personResource.addSelf(Link.with(Person.class, "fodselsnummer", contract.getElev().getFodselsNummer()));

        return personResource;
    }

}
