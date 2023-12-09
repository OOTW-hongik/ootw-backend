package OOTWhongik.OOTW.domain.clothes.exception;

public class ClothesInUseException extends RuntimeException {

    public ClothesInUseException() {
        super();
    }

    public ClothesInUseException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
