package siarhei.luskanau.iot.doorbell.interactor;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.interactor.UseCase;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class ListenDoorbellListUseCase extends UseCase<List<DoorbellEntry>, Void> {

    private final ImageRepository imageRepository;

    public ListenDoorbellListUseCase(ImageRepository imageRepository,
                                     ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.imageRepository = imageRepository;
    }

    @Override
    public Observable<List<DoorbellEntry>> buildUseCaseObservable(Void aVoid) {
        return this.imageRepository.listenDoorbellEntryList();
    }
}
