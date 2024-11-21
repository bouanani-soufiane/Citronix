package ma.yc.Citronix.farm.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.common.application.validation.UniqueValue;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "farms")
public class Farm {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private FarmId id;

    @NotBlank
    private String name;

    @NotBlank
    private String localization;

    @Positive
    private double surface;

    @NotNull
    @PastOrPresent
    private LocalDate creationDate;

    @OneToMany(mappedBy = "farm")
    private List<Field> fields = new ArrayList<>();

    @OneToMany(mappedBy = "farm")
    private List<Harvest> harvests = new ArrayList<>();

}