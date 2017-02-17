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

    public ListenImageListUseCase(ImageRepository imageRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<List<ImageEntry>> buildUseCaseObservable(Params params) {
        return this.imageRepository.listenImagesList(params.deviceId);
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
