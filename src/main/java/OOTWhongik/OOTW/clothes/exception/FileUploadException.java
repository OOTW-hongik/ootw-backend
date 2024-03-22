package OOTWhongik.OOTW.clothes.exception;

public class FileUploadException extends RuntimeException{

    public FileUploadException() {
    }

    public FileUploadException(String s) {
        super(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
