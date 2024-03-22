package OOTWhongik.OOTW.clothes.exception;

public class FileDeleteException extends RuntimeException{

    public FileDeleteException() {
    }

    public FileDeleteException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}
