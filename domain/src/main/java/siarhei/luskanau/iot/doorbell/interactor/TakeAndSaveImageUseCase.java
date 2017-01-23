package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

public class TakeAndSaveImageUseCase extends UseCase<Void, Void> {

    private final TakePictureRepository takePictureRepository;
    private final ImageRepository imageRepository;

    public TakeAndSaveImageUseCase(TakePictureRepository takePictureRepository,
                                   ImageRepository imageRepository,
                                   ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.takePictureRepository = takePictureRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(Void unused) {
        return this.takePictureRepository.takePicture()
                .flatMap(bytes -> this.imageRepository.saveImage(bytes));
    }
}
