package OOTWhongik.OOTW.domain.outfit.exception;

public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
    }

    public InvalidDateException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
