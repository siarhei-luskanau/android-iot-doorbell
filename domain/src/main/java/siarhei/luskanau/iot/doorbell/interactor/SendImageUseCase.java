package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class SendImageUseCase extends UseCase<Void, SendImageUseCase.Params> {

    private final ImageRepository imageRepository;

    public SendImageUseCase(ImageRepository imageRepository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(Params params) {
        return this.imageRepository.sendImage(params.imageBytes);
    }

    public static final class Params {
        private final byte[] imageBytes;

        private Params(byte[] imageBytes) {
            this.imageBytes = imageBytes;
        }

        public static Params forImage(byte[] imageBytes) {
            return new Params(imageBytes);
        }
    }
}
