package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class SendDeviceNameUseCase extends UseCase<Void, SendDeviceNameUseCase.Params> {

    @NonNull
    private final ImageRepository imageRepository;

    public SendDeviceNameUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
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
