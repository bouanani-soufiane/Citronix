package ma.yc.Citronix.tree.domain.model.aggregate;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

import java.time.LocalDateTime;

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
    private LocalDateTime plantingDate;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

}