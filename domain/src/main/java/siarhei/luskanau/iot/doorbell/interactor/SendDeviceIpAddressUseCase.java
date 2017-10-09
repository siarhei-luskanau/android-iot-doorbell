package siarhei.luskanau.iot.doorbell.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.IpAddressSource;

public class SendDeviceIpAddressUseCase extends UseCase<Void, SendDeviceIpAddressUseCase.Params> {

    private final ImageRepository imageRepository;
    private final IpAddressSource ipAddressSource;

    public SendDeviceIpAddressUseCase(final ImageRepository imageRepository,
                                      final IpAddressSource ipAddressSource,
                                      final ThreadExecutor threadExecutor,
                                      final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
        this.ipAddressSource = ipAddressSource;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.ipAddressSource.listenIpAddress().flatMap(pair ->
                imageRepository.sendDeviceIpAddress(params.deviceId, pair));
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
