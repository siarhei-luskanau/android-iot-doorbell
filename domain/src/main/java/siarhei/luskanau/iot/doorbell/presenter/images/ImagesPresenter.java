package siarhei.luskanau.iot.doorbell.presenter.images;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.interactor.ListenImageListUseCase;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;

public class ImagesPresenter implements Presenter {

    private static final String TAG = ImagesPresenter.class.getSimpleName();

    private final ListenImageListUseCase listenImageListUseCase;
    private final TakeAndSaveImageUseCase takeAndSaveImageUseCase;
    private final DeviceInfo deviceInfo;
    private final ErrorMessageFactory errorMessageFactory;
    private ImagesView view;


    public ImagesPresenter(ListenImageListUseCase listenImageListUseCase,
                           TakeAndSaveImageUseCase takeAndSaveImageUseCase,
                           DeviceInfo deviceInfo,
                           ErrorMessageFactory errorMessageFactory) {
        this.listenImageListUseCase = listenImageListUseCase;
        this.takeAndSaveImageUseCase = takeAndSaveImageUseCase;
        this.deviceInfo = deviceInfo;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull ImagesView view) {
        this.view = view;
    }

    public void listenDoorbell(String deviceId) {
        listenImageListUseCase.execute(new DoorbellObserver(),
                ListenImageListUseCase.Params.forParams(deviceId));
    }

    public void takeAndSaveImage(String cameraId) {
        takeAndSaveImageUseCase.execute(new ImagesPresenter.TakePictureObserver(),
                TakeAndSaveImageUseCase.Params.forParams(deviceInfo.getDeviceId(), cameraId));
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.listenImageListUseCase.dispose();
        this.view = null;
    }

    private final class DoorbellObserver extends DefaultObserver<List<ImageEntry>> {
        @Override
        public void onNext(List<ImageEntry> list) {
            ImagesPresenter.this.view.onImageListUpdated(list);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            CharSequence errorMessage = errorMessageFactory.create(e);
            ImagesPresenter.this.view.showErrorMessage(errorMessage);
        }
    }

    private final class TakePictureObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            CharSequence errorMessage = errorMessageFactory.create(e);
            ImagesPresenter.this.view.showErrorMessage(errorMessage);
        }
    }
}
