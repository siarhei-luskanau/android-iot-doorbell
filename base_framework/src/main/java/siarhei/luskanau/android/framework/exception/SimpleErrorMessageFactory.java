package siarhei.luskanau.android.framework.exception;

public class SimpleErrorMessageFactory implements ErrorMessageFactory {

    @Override
    public String create(final Throwable exception) {
        return exception.getMessage();
    }
}
