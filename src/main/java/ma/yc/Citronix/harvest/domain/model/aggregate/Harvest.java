package ma.yc.Citronix.harvest.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "harvests")
public class Harvest {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private HarvestId id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Season season;

    @NotNull
    @PastOrPresent
    private LocalDateTime date;

    @ManyToOne
    private Farm farm;


}