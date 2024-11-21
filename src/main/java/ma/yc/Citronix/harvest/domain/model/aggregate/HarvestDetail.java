package ma.yc.Citronix.harvest.domain.model.aggregate;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "harvest_details")
public class HarvestDetail {
    @EmbeddedId
    private HarvestDetailId id;

    private Double quantity;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "harvest_id", insertable = false, updatable = false)
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id", insertable = false, updatable = false)
    private Tree tree;

    public HarvestDetail(Harvest harvest, Tree tree, LocalDate date, Double quantity) {
        this.id = new HarvestDetailId(harvest.getId(), tree.getId());
        this.harvest = harvest;
        this.tree = tree;
        this.date = date;
        this.quantity = quantity;
    }
}
