package siarhei.luskanau.iot.doorbell.ui.doorbells;

import android.support.annotation.NonNull;

import java.util.List;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellListUseCase;
import siarhei.luskanau.iot.doorbell.ui.doorbells.view.IDoorbellListView;
import timber.log.Timber;

public class DoorbellListPresenter implements Presenter {

    @NonNull
    private final ListenDoorbellListUseCase listenDoorbellListUseCase;
    @NonNull
    private final ErrorMessageFactory errorMessageFactory;
    private IDoorbellListView doorbellListView;

    public DoorbellListPresenter(
            @NonNull  final ListenDoorbellListUseCase listenDoorbellListUseCase,
            @NonNull final ErrorMessageFactory errorMessageFactory
    ) {
        this.listenDoorbellListUseCase = listenDoorbellListUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull final IDoorbellListView view) {
        this.doorbellListView = view;
    }

    public void listenDoorbellList() {
        listenDoorbellListUseCase.execute(new DoorbellsObserver(), null);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.listenDoorbellListUseCase.dispose();
        this.doorbellListView = null;
    }

    private final class DoorbellsObserver extends DefaultObserver<List<DoorbellEntry>> {

        @Override
        public void onNext(final List<DoorbellEntry> doorbellEntries) {
            DoorbellListPresenter.this.doorbellListView.onDoorbellListUpdated(doorbellEntries);
        }

        @Override
        public void onError(final Throwable e) {
            Timber.d(e);
            final String errorMessage = errorMessageFactory.create(e);
            DoorbellListPresenter.this.doorbellListView.showErrorMessage(errorMessage);
        }
    }
}
