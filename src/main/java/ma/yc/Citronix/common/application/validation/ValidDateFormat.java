package ma.yc.Citronix.common.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateFormat {
    String message () default "Invalid date format. Use format: yyyy-MM-ddTHH:mm:ss";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload () default {};
}