package siarhei.luskanau.android.framework.exception;

public class SimpleErrorMessageFactory implements ErrorMessageFactory {

    @Override
    public String create(Throwable exception) {
        return exception.getMessage();
    }
}
