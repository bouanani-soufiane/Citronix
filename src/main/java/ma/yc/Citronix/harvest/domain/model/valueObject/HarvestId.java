package ma.yc.Citronix.harvest.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record HarvestId(@GeneratedValue Long value) {

    @JsonCreator
    public static HarvestId of ( Long value ) {
        return new HarvestId(value);
    }

    @JsonValue
    public Long getValue () {
        return value();
    }
}
