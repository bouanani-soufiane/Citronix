package ma.yc.Citronix.tree.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.harvest.domain.model.aggregate.HarvestDetail;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

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

    @Transient
    private Double productivity;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Field field;

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    public int getAge() {
        return calculateAge();
    }

    public Double getProductivity() {
        return calculateProductivity();
    }

    private Double calculateProductivity() {
        int age = getAge();

        if (age < 3) {
            return 2.5;
        } else if (age <= 10) {
            return 12.0;
        } else {
            return 20.0;
        }
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