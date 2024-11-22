package ma.yc.Citronix.sale.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record SaleId(@GeneratedValue Long value) {
    @JsonCreator
    public static SaleId of ( Long value ) {
        return new SaleId(value);
    }

    @JsonValue
    public Long getValue () {
        return value();
    }
}
