package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class SendDeviceInfoUseCase extends UseCase<Void, SendDeviceInfoUseCase.Params> {

    private final ImageRepository imageRepository;

    public SendDeviceInfoUseCase(final ImageRepository imageRepository,
                                 final ThreadExecutor threadExecutor,
                                 final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.imageRepository.sendDeviceInfo(params.deviceInfo);
    }

    public static final class Params {
        private final DeviceInfo deviceInfo;

        private Params(final DeviceInfo deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        public static Params forParams(final DeviceInfo deviceInfo) {
            return new Params(deviceInfo);
        }
    }
}
