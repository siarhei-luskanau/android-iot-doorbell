package siarhei.luskanau.iot.doorbell.presenter.send;

import android.support.annotation.NonNull;
import android.util.Log;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;

public class TakeAndSaveImagePresenter implements Presenter {

    private static final String TAG = TakeAndSaveImagePresenter.class.getSimpleName();

    private final TakeAndSaveImageUseCase takeAndSaveImageUseCase;
    private final DeviceInfo deviceInfo;
    private final ErrorMessageFactory errorMessageFactory;
    private TakeAndSaveImageView takeAndSaveImageView;

    public TakeAndSaveImagePresenter(TakeAndSaveImageUseCase takeAndSaveImageUseCase,
                                     DeviceInfo deviceInfo,
                                     ErrorMessageFactory errorMessageFactory) {
        this.takeAndSaveImageUseCase = takeAndSaveImageUseCase;
        this.deviceInfo = deviceInfo;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull TakeAndSaveImageView view) {
        this.takeAndSaveImageView = view;
    }

    public void takeAndSaveImage(String cameraId) {
        takeAndSaveImageUseCase.execute(new TakePictureObserver(),
                TakeAndSaveImageUseCase.Params.forParams(deviceInfo.getDeviceId(), cameraId));
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void destroy() {
        this.takeAndSaveImageUseCase.dispose();
        this.takeAndSaveImageView = null;
    }

    private final class TakePictureObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            CharSequence errorMessage = errorMessageFactory.create(e);
            TakeAndSaveImagePresenter.this.takeAndSaveImageView.showErrorMessage(errorMessage);
        }
    }
}
