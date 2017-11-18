package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

public class TakeAndSaveImageUseCase extends UseCase<Void, TakeAndSaveImageUseCase.Params> {

    @NonNull
    private final TakePictureRepository takePictureRepository;
    @NonNull
    private final ImageRepository imageRepository;

    public TakeAndSaveImageUseCase(
            @NonNull final TakePictureRepository takePictureRepository,
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet) {
        super(schedulerSet);
        this.takePictureRepository = takePictureRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.takePictureRepository.takePicture(params.cameraId)
                .flatMap(bytes -> this.imageRepository.saveImage(params.deviceId, bytes));
    }

    public static final class Params {

        private final String deviceId;
        private final String cameraId;

        private Params(final String deviceId, final String cameraId) {
            this.deviceId = deviceId;
            this.cameraId = cameraId;
        }

        public static Params forParams(final String deviceId, final String cameraId) {
            return new Params(deviceId, cameraId);
        }
    }
}
