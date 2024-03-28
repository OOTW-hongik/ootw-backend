package OOTWhongik.OOTW.outfit.exception;

public class OutfitNotFoundException extends RuntimeException {
    public OutfitNotFoundException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
