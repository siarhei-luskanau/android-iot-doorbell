package siarhei.luskanau.iot.doorbell.presenter.send;

import android.support.annotation.NonNull;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.iot.doorbell.interactor.SendImageUseCase;
import siarhei.luskanau.android.framework.presenter.Presenter;

public class SendImagePresenter implements Presenter {

    private final SendImageUseCase sendImageUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private SendImageView sendImageView;

    public SendImagePresenter(SendImageUseCase sendImageUseCase,
                              ErrorMessageFactory errorMessageFactory) {
        this.sendImageUseCase = sendImageUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull SendImageView view) {
        this.sendImageView = view;
    }

    public void sendImage(byte[] imageBytes) {
        this.sendImageUseCase.execute(new SendLampStateObserver(),
                SendImageUseCase.Params.forImage(imageBytes));
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void destroy() {
        this.sendImageUseCase.dispose();
        this.sendImageView = null;
    }

    private final class SendLampStateObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            CharSequence errorMessage = errorMessageFactory.create(e);
            SendImagePresenter.this.sendImageView.showErrorMessage(errorMessage);
        }
    }
}
