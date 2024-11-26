package ma.yc.Citronix.harvest.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.sale.domain.model.aggregate.Sale;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "harvests")
public class Harvest {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private HarvestId id;

    @Enumerated(EnumType.STRING)
    private Season season;

    @NotNull
    @PastOrPresent
    private LocalDate date;

    private Double totalQuantity;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Farm farm;

    @OneToMany(mappedBy = "harvest", fetch = FetchType.EAGER , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    @OneToOne(mappedBy = "harvest" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Sale sale;

    public void addHarvestDetail ( HarvestDetail harvestDetail) {
        if (harvestDetails == null) harvestDetails = new ArrayList<>();

        this.harvestDetails.add(harvestDetail);
        this.totalQuantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
    }
}