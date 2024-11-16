package ma.yc.Citronix.farm.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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