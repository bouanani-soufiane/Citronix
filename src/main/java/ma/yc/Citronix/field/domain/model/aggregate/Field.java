package ma.yc.Citronix.field.domain.model.aggregate;

import jakarta.persistence.*;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;

@Entity
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}