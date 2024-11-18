package ma.yc.Citronix.tree.domain.model.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ma.yc.Citronix.farm.domain.model.entity.Field;

@Entity
public class Tree {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

}