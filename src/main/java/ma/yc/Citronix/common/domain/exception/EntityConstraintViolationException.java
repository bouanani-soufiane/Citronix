package ma.yc.Citronix.common.domain.exception;

public class EntityConstraintViolationException extends RuntimeException {

    private final String entityName;
    private final String attribute;
    private final Object invalidValue;

    public EntityConstraintViolationException ( String entityName, String attribute, Object invalidValue, String message ) {
        super(String.format("The '%s' %s violated constraint with value {%s}, %s", entityName, attribute, invalidValue, message));
        this.entityName = entityName;
        this.attribute = attribute;
        this.invalidValue = invalidValue;
    }

    public String getEntityName () {
        return entityName;
    }

    public String getAttribute () {
        return attribute;
    }

    public Object getInvalidValue () {
        return invalidValue;
    }
}
