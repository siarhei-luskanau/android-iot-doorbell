package siarhei.luskanau.iot.doorbell.domain.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.domain.ImageRepository;
import siarhei.luskanau.iot.doorbell.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.doorbell.domain.executor.ThreadExecutor;

public class SendImageUseCase extends UseCase<Void, SendImageUseCase.Params> {

    private final ImageRepository imageRepository;

    public SendImageUseCase(ImageRepository imageRepository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Params params) {
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
