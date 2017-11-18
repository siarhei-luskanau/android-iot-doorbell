package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.IpAddressSource;

public class SendDeviceIpAddressUseCase extends UseCase<Void, SendDeviceIpAddressUseCase.Params> {

    @NonNull
    private final ImageRepository imageRepository;
    @NonNull
    private final IpAddressSource ipAddressSource;

    public SendDeviceIpAddressUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final IpAddressSource ipAddressSource,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
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
