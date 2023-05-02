package no.fintlabs

import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.resource.utdanning.larling.LarlingResource
import no.fintlabs.larling.LarlingEntity
import spock.lang.Specification

class LarlingEntitySpec extends Specification {

    def "test toEntity method"() {
        given: "A LarlingResource object"
        LarlingResource larlingResource = new LarlingResource()

        Identifikator systemId = new Identifikator()
        systemId.setIdentifikatorverdi("12345")
        larlingResource.setSystemId(systemId)

        larlingResource.setSystemId(systemId)

        when: "Calling the toEntity method"
        LarlingEntity larlingEntity = LarlingEntity.toEntity(larlingResource, "fintlabs.no")

        then: "Properties should match"
        larlingResource.systemId.identifikatorverdi == larlingEntity.getResource().getSystemId().getIdentifikatorverdi()
    }

}
