package siarhei.luskanau.iot.doorbell.interactor;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenDoorbellListUseCase extends UseCase<List<DoorbellEntry>, Void> {

    @NonNull
    private final ImageRepository imageRepository;

    public ListenDoorbellListUseCase(
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<List<DoorbellEntry>> buildUseCaseObservable(final Void aVoid) {
        return this.imageRepository.listenDoorbellEntryList();
    }
}
