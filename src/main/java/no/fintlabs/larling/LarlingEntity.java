package no.fintlabs.larling;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
@Entity(name = "larling")
public class LarlingEntity {

    @Id
    private String id;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private LarlingResource resource;

    private String orgId;

    private LocalDateTime lastModifiedDate;

    public static LarlingEntity toEntity(LarlingResource resource, String orgId) {
        return LarlingEntity
                .builder()
                .id(resource.getSystemId().getIdentifikatorverdi())
                .resource(resource)
                .orgId(orgId)
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }
}
