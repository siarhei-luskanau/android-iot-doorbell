package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenDoorbellUseCase extends UseCase<DoorbellEntry, ListenDoorbellUseCase.Params> {

    private final ImageRepository imageRepository;

    public ListenDoorbellUseCase(final ImageRepository imageRepository,
                                 final ThreadExecutor threadExecutor,
                                 final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<DoorbellEntry> buildUseCaseObservable(final Params params) {
        return this.imageRepository.listenDoorbellEntry(params.deviceId);
    }

    public static final class Params {
        private final String deviceId;

        private Params(final String deviceId) {
            this.deviceId = deviceId;
        }

        public static Params forParams(final String deviceId) {
            return new Params(deviceId);
        }
    }
}
