package OOTWhongik.OOTW.outfit.exception;

public class CrawlingException extends RuntimeException {
    public CrawlingException(String message) {
        super(message);
    }

//    날씨 정보 예외는 계층구조가 복잡해 stack trace 를 살려놓는다.
//    @Override
//    public synchronized Throwable fillInStackTrace() {
//        return this;
//    }
}
