package siarhei.luskanau.iot.doorbell.domain.exception;

public class SimpleErrorMessageFactory implements ErrorMessageFactory {

    @Override
    public String create(Throwable exception) {
        return exception.getMessage();
    }
}
