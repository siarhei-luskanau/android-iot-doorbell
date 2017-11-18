package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class SendDeviceInfoUseCase extends UseCase<Void, SendDeviceInfoUseCase.Params> {

    @NonNull
    private final ImageRepository imageRepository;

    public SendDeviceInfoUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
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
