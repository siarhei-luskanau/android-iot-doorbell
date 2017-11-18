package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenDoorbellUseCase extends UseCase<DoorbellEntry, ListenDoorbellUseCase.Params> {

    @NonNull
    private final ImageRepository imageRepository;

    public ListenDoorbellUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
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
