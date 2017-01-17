package siarhei.luskanau.iot.doorbell.presenter.send;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.doorbell.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.doorbell.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.doorbell.domain.interactor.SendImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.Presenter;

public class SendLampStatePresenter implements Presenter {

    private final SendImageUseCase sendImageUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private SendLampStateView sendLampStateView;

    public SendLampStatePresenter(SendImageUseCase sendImageUseCase,
                                  ErrorMessageFactory errorMessageFactory) {
        this.sendImageUseCase = sendImageUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull SendLampStateView view) {
        this.sendLampStateView = view;
    }

    public void sendLampState(Boolean lampState) {
        this.sendImageUseCase.execute(new SendLampStateObserver(),
                SendImageUseCase.Params.forLampState(lampState));
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void destroy() {
        this.sendImageUseCase.dispose();
        this.sendLampStateView = null;
    }

    private final class SendLampStateObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            CharSequence errorMessage = errorMessageFactory.create(e);
            SendLampStatePresenter.this.sendLampStateView.showErrorMessage(errorMessage);
        }
    }
}
