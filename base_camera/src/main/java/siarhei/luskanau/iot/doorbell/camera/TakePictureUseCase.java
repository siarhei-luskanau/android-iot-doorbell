package siarhei.luskanau.iot.doorbell.camera;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;

public class TakePictureUseCase extends UseCase<byte[], Void> {

    private final CameraRepository cameraRepository;

    public TakePictureUseCase(CameraRepository cameraRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.cameraRepository = cameraRepository;
    }

    @Override
    public Observable<byte[]> buildUseCaseObservable(Void unused) {
        return this.cameraRepository.takePicture();
    }

}
