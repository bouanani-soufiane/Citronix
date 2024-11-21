package ma.yc.Citronix.common.domain.exception;

public class EntityConstraintViolationException extends RuntimeException {

    private final String entityName;
    private final String attribute;
    private final Object invalidValue;

    public EntityConstraintViolationException(String entityName, String attribute, Object invalidValue, String message) {
        super(buildMessage(entityName, attribute, invalidValue, message));
        this.entityName = entityName;
        this.attribute = attribute;
        this.invalidValue = invalidValue;
    }

    private static String buildMessage(String entityName, String attribute, Object invalidValue, String message) {
        return String.format(
                "The '%s' attribute of the '%s' entity has an invalid value: '%s'. %s",
                attribute, entityName, invalidValue, message
        );
    }

    public String getEntityName() {
        return entityName;
    }

    public String getAttribute() {
        return attribute;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }
}
