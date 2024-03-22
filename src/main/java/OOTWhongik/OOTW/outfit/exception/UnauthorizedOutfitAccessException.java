package OOTWhongik.OOTW.outfit.exception;

public class UnauthorizedOutfitAccessException extends RuntimeException {

    public UnauthorizedOutfitAccessException() {
    }

    public UnauthorizedOutfitAccessException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
