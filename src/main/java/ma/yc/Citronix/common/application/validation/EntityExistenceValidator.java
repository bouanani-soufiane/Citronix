package ma.yc.Citronix.common.application.validation;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntityExistenceValidator implements ConstraintValidator<EntityExists, Object> {

    private final EntityManager entityManager;
    private Class<?> entityClass;

    @Override
    public void initialize ( EntityExists constraintAnnotation ) {
        this.entityClass = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid ( Object value, ConstraintValidatorContext context ) {
        if (value == null) {
            return true;
        }

        String queryStr = String.format("SELECT COUNT(e) FROM %s e WHERE e.id = :value", entityClass.getSimpleName());
        Long count = entityManager.createQuery(queryStr, Long.class).setParameter("value", value).getSingleResult();

        return count > 0;
    }
}
