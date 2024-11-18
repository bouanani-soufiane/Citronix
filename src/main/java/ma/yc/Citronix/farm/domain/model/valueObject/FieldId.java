package ma.yc.Citronix.farm.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record FieldId(@GeneratedValue Long value) {
    @JsonCreator
    public static FieldId of(Long value) {
        return new FieldId(value);
    }

    @JsonValue
    public Long getValue() {
        return value();
    }
}
