package OOTWhongik.OOTW.clothes.exception;

public class UnauthorizedClothesAccessException extends RuntimeException {

    public UnauthorizedClothesAccessException () {
        super();
    }

    public UnauthorizedClothesAccessException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
