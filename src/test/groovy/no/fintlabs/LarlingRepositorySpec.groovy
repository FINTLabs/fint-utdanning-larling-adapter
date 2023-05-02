package no.fintlabs

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.utdanning.larling.LarlingResource
import no.fintlabs.adapter.models.RequestFintEvent
import no.fintlabs.larling.LarlingJpaRepository
import no.fintlabs.larling.LarlingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@DataJpaTest
class LarlingRepositorySpec extends Specification {

    @Autowired
    LarlingJpaRepository larlingJpaRepository

    def setup() {
        // Clean up the repository before each test
        larlingJpaRepository.deleteAll()
    }

    def "test saveResources method"() {
        given: "a larlingResource and a RequestFintEvent"
        LarlingResource larlingResource = new LarlingResource()
        RequestFintEvent requestFintEvent = new RequestFintEvent()

        requestFintEvent.setOrgId("fintlabs.no")

        Identifikator systemId = new Identifikator()
        systemId.setIdentifikatorverdi("12345")
        larlingResource.setSystemId(systemId)

        and: "a repository instance"
        def larlingRepository = new LarlingRepository(larlingJpaRepository)

        when: "saveResources is called"
        def savedResource = larlingRepository.saveResources(larlingResource, requestFintEvent)

        then: "the resulting larlingResource should have the same values"
        savedResource == larlingResource

        and: "an LarlingEntity should be persisted with the same values"
        def entity = larlingJpaRepository.findById("12345").orElse(null)
        entity != null
        entity.resource == larlingResource
        entity.orgId == "fintlabs.no"
    }

}
