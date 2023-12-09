package OOTWhongik.OOTW.domain.clothes.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super();
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
