package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

public class TakeAndSaveImageUseCase extends UseCase<Void, TakeAndSaveImageUseCase.Params> {

    private final TakePictureRepository takePictureRepository;
    private final ImageRepository imageRepository;

    public TakeAndSaveImageUseCase(final TakePictureRepository takePictureRepository,
                                   final ImageRepository imageRepository,
                                   final ThreadExecutor threadExecutor,
                                   final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
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
