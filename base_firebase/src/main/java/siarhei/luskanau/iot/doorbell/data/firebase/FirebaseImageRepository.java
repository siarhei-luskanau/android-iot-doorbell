package siarhei.luskanau.iot.doorbell.data.firebase;

import android.util.Base64;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class FirebaseImageRepository implements ImageRepository {

    @Override
    public Observable<Void> saveImage(final byte[] imageBytes) {
        return Observable.defer(() -> {
            final DatabaseReference log = FirebaseDatabase.getInstance().getReference("logs").push();
            String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
            log.child("timestamp").setValue(ServerValue.TIMESTAMP);
            log.child("image").setValue(imageStr);
            log.child("image_length").setValue(imageBytes.length);
            return Observable.empty();
        });
    }
}
