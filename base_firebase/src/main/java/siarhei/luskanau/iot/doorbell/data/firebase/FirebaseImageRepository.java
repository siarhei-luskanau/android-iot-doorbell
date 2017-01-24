package siarhei.luskanau.iot.doorbell.data.firebase;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class FirebaseImageRepository implements ImageRepository {

    private static final String TAG = FirebaseImageRepository.class.getSimpleName();
    private final Context context;

    public FirebaseImageRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Void> saveImage(final byte[] imageBytes) {
        return Observable.defer(() -> {
            final DatabaseReference log = FirebaseDatabase.getInstance().getReference("logs").push();
            String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
            log.child("timestamp").setValue(ServerValue.TIMESTAMP);
            log.child("image_length").setValue(imageBytes.length);
            log.child("image").setValue(imageStr);

            Map<String, Float> annotations = new HashMap<>();

            //detect TextBlocks
            try {
                Frame frame = new Frame.Builder()
                        .setBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length))
                        .build();

                SparseArray<TextBlock> textBlocks = new TextRecognizer.Builder(context).build().detect(frame);

                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(i);
                    annotations.put(textBlock.getValue(), (float) i);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            // detect faces
            try {
                Frame frame = new Frame.Builder()
                        .setBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length))
                        .build();

                SparseArray<Face> faces = new FaceDetector.Builder(context).build().detect(frame);
                if (faces.size() > 0) {
                    annotations.put("Faces: " + faces.size(), (float) annotations.size());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            log.child("annotations").setValue(annotations);

            return Observable.empty();
        });
    }
}
