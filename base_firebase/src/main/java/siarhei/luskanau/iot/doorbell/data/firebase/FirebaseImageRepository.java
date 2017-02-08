package siarhei.luskanau.iot.doorbell.data.firebase;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.util.Pair;
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
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class FirebaseImageRepository implements ImageRepository {

    private static final String TAG = FirebaseImageRepository.class.getSimpleName();
    private static final String DOORBELLS_KEY = "doorbells";
    private static final String IMAGES_KEY = "images";
    private static final String ADDITIONAL_INFO = "additional_info";
    private static final String DEVICES_ID = "device_id";
    private static final String BUILD_DEVICES = "build_device";
    private static final String BUILD_MODEL = "build_model";
    private static final String BUILD_VERSION_SDK_INT = "build_version_sdk_int";
    private static final String BUILD_VERSION_RELEASE = "build_version_release";
    private static final String IP_ADDRESS = "ip_address";

    private final Context context;

    public FirebaseImageRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Void> saveImage(String deviceId, byte[] imageBytes) {
        return Observable.defer(() -> {
            final DatabaseReference log = FirebaseDatabase.getInstance().getReference(DOORBELLS_KEY).child(deviceId)
                    .child(IMAGES_KEY).push();
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

    @Override
    public Observable<Void> sendDeviceInfo(DeviceInfo deviceInfo) {
        return Observable.defer(() -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference(DOORBELLS_KEY)
                    .child(deviceInfo.getDeviceId());
            databaseReference.child(DEVICES_ID).setValue(deviceInfo.getDeviceId());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_DEVICES).setValue(deviceInfo.getBuildDevice());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_MODEL).setValue(deviceInfo.getBuildModel());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_VERSION_SDK_INT).setValue(deviceInfo.getBuildVersionSdkInt());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_VERSION_RELEASE).setValue(deviceInfo.getBuildVersionRelease());
            Gson gson = new Gson();
            if (deviceInfo.getAdditionalInfo() != null) {
                for (String index : deviceInfo.getAdditionalInfo().keySet()) {
                    databaseReference.child(ADDITIONAL_INFO).child(index)
                            .setValue(gson.toJson(deviceInfo.getAdditionalInfo().get(index)));
                }
            }
            return Observable.empty();
        });
    }

    @Override
    public Observable<Void> sendDeviceIpAddress(String deviceId, Pair<String, String> ipAddress) {
        return Observable.defer(() -> {
            FirebaseDatabase.getInstance()
                    .getReference(DOORBELLS_KEY)
                    .child(deviceId)
                    .child(ADDITIONAL_INFO)
                    .child(IP_ADDRESS)
                    .child(ipAddress.first).setValue(ipAddress.second);
            return Observable.empty();
        });
    }
}
