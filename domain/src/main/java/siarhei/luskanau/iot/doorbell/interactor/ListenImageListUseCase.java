package siarhei.luskanau.iot.doorbell.interactor;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenImageListUseCase extends UseCase<List<ImageEntry>, ListenImageListUseCase.Params> {

    private final ImageRepository imageRepository;

    public ListenImageListUseCase(final ImageRepository imageRepository,
                                  final ThreadExecutor threadExecutor,
                                  final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<List<ImageEntry>> buildUseCaseObservable(final Params params) {
        return this.imageRepository.listenImagesList(params.deviceId);
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
