package ma.yc.Citronix.tree.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "trees")
public class Tree {

    @Id
    private TreeId id;

    @Column(name = "planting_date")
    @PastOrPresent
    private LocalDate plantingDate;

    @Transient
    private int age;


    @ManyToOne
    private Field field;

    public int getAge() {
        return calculateAge();
    }

    private int calculateAge() {
        LocalDate currentDate = LocalDate.now();
        assert plantingDate != null;

        Period period = Period.between(plantingDate, currentDate);
        int years = period.getYears();
        int months = period.getMonths();
        return years + (months / 12);
    }
}