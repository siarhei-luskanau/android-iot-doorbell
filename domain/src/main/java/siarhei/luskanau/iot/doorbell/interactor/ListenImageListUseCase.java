package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenImageListUseCase extends UseCase<List<ImageEntry>, ListenImageListUseCase.Params> {

    @NonNull
    private final ImageRepository imageRepository;

    public ListenImageListUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
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
