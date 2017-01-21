package siarhei.luskanau.iot.doorbell.camera;

import android.content.Context;

import io.reactivex.Observable;

public class CameraRepository {

    private final Context context;

    public CameraRepository(Context context) {
        this.context = context;
    }

    public Observable<byte[]> takePicture() {
        return Observable.create(emitter -> {
            emitter.onNext(new byte[0]);
            emitter.onNext(new byte[1]);
            emitter.onNext(new byte[2]);
            emitter.onComplete();
        });
    }
}
