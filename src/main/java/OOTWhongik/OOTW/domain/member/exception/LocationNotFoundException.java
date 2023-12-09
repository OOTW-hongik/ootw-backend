package OOTWhongik.OOTW.domain.member.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException () {
        super();
    }

    public LocationNotFoundException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
