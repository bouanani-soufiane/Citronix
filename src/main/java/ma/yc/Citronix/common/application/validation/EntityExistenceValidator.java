package ma.yc.Citronix.common.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntityExistenceValidator implements ConstraintValidator<EntityExists, Object> {


    @Override
    public void initialize ( EntityExists constraintAnnotation ) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid ( Object o, ConstraintValidatorContext constraintValidatorContext ) {
        return false;
    }
}