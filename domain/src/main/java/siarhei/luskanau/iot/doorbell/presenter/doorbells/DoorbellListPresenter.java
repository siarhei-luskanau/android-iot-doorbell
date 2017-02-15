package siarhei.luskanau.iot.doorbell.presenter.doorbells;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.presenter.Presenter;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellListUseCase;

public class DoorbellListPresenter implements Presenter {

    private static final String TAG = DoorbellListPresenter.class.getSimpleName();

    private final ListenDoorbellListUseCase listenDoorbellListUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private DoorbellListView doorbellListView;


    public DoorbellListPresenter(ListenDoorbellListUseCase listenDoorbellListUseCase,
                                 ErrorMessageFactory errorMessageFactory) {
        this.listenDoorbellListUseCase = listenDoorbellListUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull DoorbellListView view) {
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
        public void onNext(List<DoorbellEntry> doorbellEntries) {
            DoorbellListPresenter.this.doorbellListView.onDoorbellListUpdated(doorbellEntries);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
            CharSequence errorMessage = errorMessageFactory.create(e);
            DoorbellListPresenter.this.doorbellListView.showErrorMessage(errorMessage);
        }
    }
}
