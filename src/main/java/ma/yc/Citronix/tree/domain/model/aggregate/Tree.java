package ma.yc.Citronix.tree.domain.model.aggregate;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.entity.Field;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

}