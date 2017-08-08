package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class SendDeviceNameUseCase extends UseCase<Void, SendDeviceNameUseCase.Params> {

    private final ImageRepository imageRepository;

    public SendDeviceNameUseCase(final ImageRepository imageRepository,
                                 final ThreadExecutor threadExecutor,
                                 final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.imageRepository.sendDeviceName(params.deviceId, params.deviceName);
    }

    public static final class Params {

        private final String deviceId;
        private final String deviceName;

        private Params(final String deviceId, final String deviceName) {
            this.deviceId = deviceId;
            this.deviceName = deviceName;
        }

        public static Params forParams(final String deviceId, final String deviceName) {
            return new Params(deviceId, deviceName);
        }
    }
}
