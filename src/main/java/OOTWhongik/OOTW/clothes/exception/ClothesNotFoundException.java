package OOTWhongik.OOTW.clothes.exception;

public class ClothesNotFoundException extends RuntimeException {
    public ClothesNotFoundException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
