package ma.yc.Citronix.tree.domain.model.valueObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record TreeId(@GeneratedValue Long value) {
    @JsonCreator
    public static TreeId of ( Long value ) {
        return new TreeId(value);
    }

    @JsonValue
    public Long getValue () {
        return value();
    }
}
