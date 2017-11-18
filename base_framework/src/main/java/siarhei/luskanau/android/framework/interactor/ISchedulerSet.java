package siarhei.luskanau.android.framework.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

public interface ISchedulerSet {

    @NonNull
    Scheduler subscribeOn();

    @NonNull
    Scheduler observeOn();
}
