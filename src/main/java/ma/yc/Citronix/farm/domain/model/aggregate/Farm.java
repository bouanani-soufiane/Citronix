package ma.yc.Citronix.farm.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.field.domain.model.aggregate.Field;

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
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "farm")
    private List<Field> fields = new ArrayList<>();

}