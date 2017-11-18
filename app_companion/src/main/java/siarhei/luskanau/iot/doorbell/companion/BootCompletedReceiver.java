package siarhei.luskanau.iot.doorbell.companion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class BootCompletedReceiver extends BroadcastReceiver {

    public BootCompletedReceiver() {
    }

    public void onReceive(final Context context, final Intent intent) {
        Timber.d(intent.getAction());
    }
}