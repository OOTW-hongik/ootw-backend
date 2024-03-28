package OOTWhongik.OOTW.clothes.exception;

public class FileUploadException extends RuntimeException{
    public FileUploadException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
