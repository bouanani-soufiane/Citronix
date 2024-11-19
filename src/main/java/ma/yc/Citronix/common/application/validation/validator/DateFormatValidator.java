package ma.yc.Citronix.common.application.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ma.yc.Citronix.common.application.validation.ValidDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, LocalDateTime> {
    @Override
    public boolean isValid ( LocalDateTime value, ConstraintValidatorContext context ) {
        if (value == null) {
            return true;
        }
        try {
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}