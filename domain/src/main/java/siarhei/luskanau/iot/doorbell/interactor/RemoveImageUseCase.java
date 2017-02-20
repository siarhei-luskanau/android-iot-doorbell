package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class RemoveImageUseCase extends UseCase<Void, RemoveImageUseCase.Params> {

    private final ImageRepository imageRepository;

    public RemoveImageUseCase(ImageRepository imageRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(Params params) {
        return this.imageRepository.removeImage(params.deviceId, params.imageId);
    }

    public static final class Params {
        private final String deviceId;
        private final String imageId;

        private Params(String deviceId, String imageId) {
            this.deviceId = deviceId;
            this.imageId = imageId;
        }

        public static Params forParams(String deviceId, String imageId) {
            return new Params(deviceId, imageId);
        }
    }
}
