package OOTWhongik.OOTW.clothes.exception;

public class ClothesInUseException extends RuntimeException {
    public ClothesInUseException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
