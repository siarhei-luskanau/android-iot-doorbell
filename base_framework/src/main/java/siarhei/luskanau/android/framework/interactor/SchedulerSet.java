package siarhei.luskanau.android.framework.interactor;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerSet implements ISchedulerSet {

    @NonNull
    @Override
    public Scheduler subscribeOn() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler observeOn() {
        return AndroidSchedulers.mainThread();
    }
}
