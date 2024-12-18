package ma.yc.Citronix.sale.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Accessors;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "sales")
public class Sale {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private SaleId id;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String client;

    @OneToOne
    private Harvest harvest;

    @Positive
    @Column(name = "unit_price")
    private Double unitPrice;

    @Transient
    private Double income;

    public Double getIncome(){
        return calculateIncome();
    }

    public Double calculateIncome() {
        return unitPrice * harvest.getTotalQuantity();
    }

}