package ma.yc.Citronix.sale.domain.exception;

public class HarvestAlreadySoldException extends RuntimeException {

    public HarvestAlreadySoldException(String message) {
        super(message);
    }

    public HarvestAlreadySoldException(String message, Throwable cause) {
        super(message, cause);
    }
}