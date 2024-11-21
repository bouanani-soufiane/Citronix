package ma.yc.Citronix.sale.domain.model.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record SaleId(@GeneratedValue Long value) {
}
