package OOTWhongik.OOTW.global.exception;

public class CrawlingException extends RuntimeException {

    public CrawlingException() {
    }

    public CrawlingException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
