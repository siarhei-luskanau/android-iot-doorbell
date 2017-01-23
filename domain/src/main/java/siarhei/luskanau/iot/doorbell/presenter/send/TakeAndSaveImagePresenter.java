package siarhei.luskanau.iot.doorbell.presenter.send;

import android.support.annotation.NonNull;
import android.util.Log;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;

public class TakeAndSaveImagePresenter implements Presenter {

    private static final String TAG = TakeAndSaveImagePresenter.class.getSimpleName();

    private final TakeAndSaveImageUseCase takeAndSaveImageUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private SendImageView sendImageView;

    public TakeAndSaveImagePresenter(TakeAndSaveImageUseCase takeAndSaveImageUseCase,
                                     ErrorMessageFactory errorMessageFactory) {
        this.takeAndSaveImageUseCase = takeAndSaveImageUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull SendImageView view) {
        this.sendImageView = view;
    }

    public void takeAndSaveImage() {
        takeAndSaveImageUseCase.execute(new TakePictureObserver(), null);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void destroy() {
        this.takeAndSaveImageUseCase.dispose();
        this.sendImageView = null;
    }

    private final class TakePictureObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            CharSequence errorMessage = errorMessageFactory.create(e);
            TakeAndSaveImagePresenter.this.sendImageView.showErrorMessage(errorMessage);
        }
    }
}
