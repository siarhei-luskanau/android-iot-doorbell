package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenDoorbellUseCase extends UseCase<DoorbellEntry, ListenDoorbellUseCase.Params> {

    private final ImageRepository imageRepository;

    public ListenDoorbellUseCase(ImageRepository imageRepository,
                                 ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<DoorbellEntry> buildUseCaseObservable(Params params) {
        return this.imageRepository.listenDoorbellEntry(params.deviceId);
    }

    public static final class Params {
        private final String deviceId;

        private Params(String deviceId) {
            this.deviceId = deviceId;
        }

        public static Params forParams(String deviceId) {
            return new Params(deviceId);
        }
    }
}
