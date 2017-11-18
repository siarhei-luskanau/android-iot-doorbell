package siarhei.luskanau.iot.doorbell.presenter.send;

import android.support.annotation.NonNull;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import timber.log.Timber;

public class TakeAndSaveImagePresenter implements Presenter {

    @NonNull
    private final TakeAndSaveImageUseCase takeAndSaveImageUseCase;
    @NonNull
    private final DeviceInfo deviceInfo;
    @NonNull
    private final ErrorMessageFactory errorMessageFactory;
    private TakeAndSaveImageView takeAndSaveImageView;

    public TakeAndSaveImagePresenter(
            @NonNull final TakeAndSaveImageUseCase takeAndSaveImageUseCase,
            @NonNull final DeviceInfo deviceInfo,
            @NonNull final ErrorMessageFactory errorMessageFactory
    ) {
        this.takeAndSaveImageUseCase = takeAndSaveImageUseCase;
        this.deviceInfo = deviceInfo;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull final TakeAndSaveImageView view) {
        this.takeAndSaveImageView = view;
    }

    public void takeAndSaveImage(final String cameraId) {
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
        public void onError(final Throwable e) {
            Timber.d(e);
            final CharSequence errorMessage = errorMessageFactory.create(e);
            TakeAndSaveImagePresenter.this.takeAndSaveImageView.showErrorMessage(errorMessage);
        }
    }
}
