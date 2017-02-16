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
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class FirebaseImageRepository implements ImageRepository {

    private static final String TAG = FirebaseImageRepository.class.getSimpleName();
    private static final String DOORBELLS_KEY = "doorbells";
    private static final String IMAGES_KEY = "images";
    private static final String RING_KEY = "ring";
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
            DatabaseReference deviceDatabaseReference = FirebaseDatabase.getInstance()
                    .getReference(DOORBELLS_KEY).child(deviceId);
            deviceDatabaseReference.child(RING_KEY).setValue(Boolean.FALSE);
            DatabaseReference imagesDatabaseReference = deviceDatabaseReference.child(IMAGES_KEY).push();
            String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
            imagesDatabaseReference.child("timestamp").setValue(ServerValue.TIMESTAMP);
            imagesDatabaseReference.child("image_length").setValue(imageBytes.length);
            imagesDatabaseReference.child("image").setValue(imageStr);

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
            imagesDatabaseReference.child("annotations").setValue(annotations);

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
            putMap(databaseReference.child(ADDITIONAL_INFO), deviceInfo.getAdditionalInfo());
            return Observable.empty();
        });
    }

    private void putMap(DatabaseReference databaseReference, Map map) {
        Gson gson = new Gson();
        if (map != null) {
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof Map) {
                    Map valueMap = (Map) value;
                    putMap(databaseReference.child(String.valueOf(key)), valueMap);
                } else {
                    if (value instanceof String) {
                        databaseReference.child(String.valueOf(key)).setValue(value);
                    } else {
                        databaseReference.child(String.valueOf(key)).setValue(gson.toJson(value));
                    }
                }
            }
        }
    }

    @Override
    public Observable<DoorbellEntry> listenDoorbellEntry(String deviceId) {
        return Observable.create(emitter -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DOORBELLS_KEY).child(deviceId);
            databaseReference.addValueEventListener(new DoorbellEntryValueEventListener(emitter, databaseReference));
        });
    }

    @Override
    public Observable<List<DoorbellEntry>> listenDoorbellEntryList() {
        Observable<Map<String, DoorbellEntry>> observable = Observable.create(emitter -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DOORBELLS_KEY);
            databaseReference.addValueEventListener(new DoorbellEntryMapValueEventListener(emitter, databaseReference));
        });
        return observable.map(map -> new ArrayList<>(map.values()));
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

    private static class DoorbellEntryValueEventListener extends EmitterValueEventListener<DoorbellEntry> {
        public DoorbellEntryValueEventListener(ObservableEmitter<DoorbellEntry> emitter, Query query) {
            super(emitter, query);
        }
    }

    private static class DoorbellEntryMapValueEventListener
            extends EmitterValueEventListener<Map<String, DoorbellEntry>> {
        public DoorbellEntryMapValueEventListener(ObservableEmitter<Map<String, DoorbellEntry>> emitter, Query query) {
            super(emitter, query);
        }
    }
}
