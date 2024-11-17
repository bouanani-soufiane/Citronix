package ma.yc.Citronix.farm.domain.model.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record FarmId(@GeneratedValue Long value) {
}
