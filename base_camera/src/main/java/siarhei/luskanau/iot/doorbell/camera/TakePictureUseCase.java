package siarhei.luskanau.iot.doorbell.camera;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;

public class TakePictureUseCase extends UseCase<byte[], Void> {

    private final static int IMAGE_MAX_SIZE = 800;
    private final CameraRepository cameraRepository;
    private final ImageCompressor imageCompressor;

    public TakePictureUseCase(CameraRepository cameraRepository, ImageCompressor imageScale, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.cameraRepository = cameraRepository;
        this.imageCompressor = imageScale;
    }

    @Override
    public Observable<byte[]> buildUseCaseObservable(Void unused) {
        Observable<byte[]> observable = this.cameraRepository.takePicture();
        return imageCompressor.scale(observable, IMAGE_MAX_SIZE);
    }
}
