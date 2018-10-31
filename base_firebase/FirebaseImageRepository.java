package siarhei.luskanau.iot.doorbell.data.firebase;

import android.support.v4.util.*;

import durdinapps.rxfirebase2.*;
import siarhei.luskanau.iot.doorbell.repository.*;

public class FirebaseImageRepository implements ImageRepository {

    private static final String DOORBELL_APP_KEY = "doorbell_app";
    private static final String DEVICES_KEY = "devices";

    private static final String BUILD_DEVICES = "build_device";
    private static final String BUILD_MODEL = "build_model";
    private static final String BUILD_VERSION_SDK_INT = "build_version_sdk_int";
    private static final String BUILD_VERSION_RELEASE = "build_version_release";
    private static final String ADDITIONAL_INFO = "additional_info";
    private static final String IP_ADDRESS = "ip_address";

    private final Context context;

    public FirebaseImageRepository(final Context context) {
        this.context = context;
    }

    private static DatabaseReference getAppDatabase() {
        return FirebaseDatabase.getInstance()
                .getReference(DOORBELL_APP_KEY);
    }

    @Override
    public Observable<Void> saveImage(final String deviceId, final byte[] imageBytes) {
        return Observable.defer(() -> {
            final List<String> annotations = getAnnotations(imageBytes);

            final DatabaseReference deviceDatabaseReference = getAppDatabase()
                    .child(DEVICES_KEY)
                    .child(deviceId).child(DomainConstants.IMAGES).push();
            final String imageId = deviceDatabaseReference.getKey();
            putImageEntry(deviceDatabaseReference, imageId, annotations, imageBytes.length, null);

            final DatabaseReference imageDatabaseReference = getAppDatabase()
                    .child(DomainConstants.IMAGES)
                    .child(deviceId).child(imageId);
            putImageEntry(imageDatabaseReference, imageId, annotations, imageBytes.length, imageBytes);

            getAppDatabase().child(DEVICES_KEY)
                    .child(deviceId).child(DomainConstants.RING).setValue(Boolean.FALSE);
            return Observable.empty();
        });
    }

    private void putImageEntry(final DatabaseReference databaseReference, final String imageId,
                               final List<String> annotations, final int length, final byte[] imageBytes) {
        databaseReference.child(DomainConstants.IMAGE_ID).setValue(imageId);
        databaseReference.child(DomainConstants.TIMESTAMP).setValue(ServerValue.TIMESTAMP);
        databaseReference.child(DomainConstants.ANNOTATIONS).setValue(annotations);
        databaseReference.child(DomainConstants.IMAGE_LENGTH).setValue(length);
        if (imageBytes != null) {
            final String imageStr = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            databaseReference.child(DomainConstants.IMAGE).setValue(imageStr);
        }
    }

    @Override
    public Observable<Void> sendDeviceInfo(final DeviceInfo deviceInfo) {
        return Observable.defer(() -> {
            final DatabaseReference databaseReference = getAppDatabase()
                    .child(DEVICES_KEY)
                    .child(deviceInfo.getDeviceId());
            databaseReference.child(DomainConstants.DEVICE_ID).setValue(deviceInfo.getDeviceId());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_DEVICES).setValue(deviceInfo.getBuildDevice());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_MODEL).setValue(deviceInfo.getBuildModel());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_VERSION_SDK_INT).setValue(deviceInfo.getBuildVersionSdkInt());
            databaseReference.child(ADDITIONAL_INFO).child(BUILD_VERSION_RELEASE).setValue(deviceInfo.getBuildVersionRelease());
            putMap(databaseReference.child(ADDITIONAL_INFO), deviceInfo.getAdditionalInfo());
            return Observable.empty();
        });
    }

    @Override
    public Observable<Void> sendDeviceName(final String deviceId, final String deviceName) {
        return Observable.defer(() -> {
            final DatabaseReference databaseReference = getAppDatabase()
                    .child(DEVICES_KEY)
                    .child(deviceId);
            databaseReference.child(DomainConstants.NAME).setValue(deviceName);
            return Observable.empty();
        });
    }

    private void putMap(final DatabaseReference databaseReference, final Map map) {
        final Gson gson = new Gson();
        if (map != null) {
            for (final Object key : map.keySet()) {
                final Object value = map.get(key);
                if (value instanceof Map) {
                    final Map valueMap = (Map) value;
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
    public Observable<DoorbellEntry> listenDoorbellEntry(final String deviceId) {
        return Observable.create(emitter -> {
            final DatabaseReference databaseReference = getAppDatabase()
                    .child(DEVICES_KEY).child(deviceId);
            databaseReference.addValueEventListener(new DoorbellEntryValueEventListener(emitter, databaseReference));
        });
    }

    @Override
    public Observable<List<DoorbellEntry>> listenDoorbellEntryList() {
        final Observable<Map<String, DoorbellEntry>> observable = Observable.create(emitter -> {
            final DatabaseReference databaseReference = getAppDatabase()
                    .child(DEVICES_KEY);
            databaseReference.addValueEventListener(new DoorbellEntryMapValueEventListener(emitter, databaseReference));
        });
        return observable.map(map -> new ArrayList<>(map.values()));
    }

    @Override
    public Observable<List<ImageEntry>> listenImagesList(final String deviceId) {
        final Observable<Map<String, ImageEntry>> observable = Observable.create(emitter -> {
            final DatabaseReference databaseReference = getAppDatabase()
                    .child(DomainConstants.IMAGES).child(deviceId);
            databaseReference.addValueEventListener(new ImagesEntryMapValueEventListener(emitter, databaseReference));
        });
        return observable.map(map -> new ArrayList<>(map.values()));
    }

    @Override
    public Observable<Void> removeImage(final String deviceId, final String imageId) {
        return Observable.defer(() -> {
            getAppDatabase().child(DomainConstants.IMAGES)
                    .child(deviceId).child(imageId).removeValue();
            getAppDatabase().child(DEVICES_KEY).child(deviceId)
                    .child(DomainConstants.IMAGES).child(imageId).removeValue();
            return Observable.empty();
        });
    }

    @Override
    public Observable<Void> sendDeviceIpAddress(final String deviceId, final Pair<String, String> ipAddress) {
        return RxFirebaseDatabase.setValue(
                getAppDatabase()
                        .child(DEVICES_KEY)
                        .child(deviceId)
                        .child(ADDITIONAL_INFO)
                        .child(IP_ADDRESS)
                        .child(ipAddress.first),
                ipAddress.second
        )
                .toObservable();
    }

    private List<String> getAnnotations(final byte[] imageBytes) {
        final List<String> annotations = new ArrayList<>();
        //detect TextBlocks
        try {
            final Frame frame = new Frame.Builder()
                    .setBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length))
                    .build();

            final SparseArray<TextBlock> textBlocks = new TextRecognizer.Builder(context).build().detect(frame);

            for (int i = 0; i < textBlocks.size(); i++) {
                final TextBlock textBlock = textBlocks.get(i);
                annotations.add(textBlock.getValue());
            }
        } catch (final Exception e) {
            Timber.e(e);
        }
        // detect faces
        try {
            final Frame frame = new Frame.Builder()
                    .setBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length))
                    .build();

            final SparseArray<Face> faces = new FaceDetector.Builder(context).build().detect(frame);
            if (faces.size() > 0) {
                annotations.add("Faces: " + faces.size());
            }
        } catch (final Exception e) {
            Timber.e(e);
        }
        return annotations;
    }

    private static class DoorbellEntryValueEventListener extends EmitterValueEventListener<DoorbellEntry> {

        public DoorbellEntryValueEventListener(final ObservableEmitter<DoorbellEntry> emitter, final Query query) {
            super(emitter, query);
        }
    }

    private static class DoorbellEntryMapValueEventListener
            extends EmitterValueEventListener<Map<String, DoorbellEntry>> {

        public DoorbellEntryMapValueEventListener(final ObservableEmitter<Map<String, DoorbellEntry>> emitter, final Query query) {
            super(emitter, query);
        }

        @Override
        protected Map<String, DoorbellEntry> checkValue(final Map<String, DoorbellEntry> value) {
            if (value == null) {
                return Collections.emptyMap();
            }
            return super.checkValue(value);
        }
    }

    private static class ImagesEntryMapValueEventListener
            extends EmitterValueEventListener<Map<String, ImageEntry>> {

        public ImagesEntryMapValueEventListener(final ObservableEmitter<Map<String, ImageEntry>> emitter, final Query query) {
            super(emitter, query);
        }

        @Override
        protected Map<String, ImageEntry> checkValue(final Map<String, ImageEntry> value) {
            if (value == null) {
                return Collections.emptyMap();
            }
            return super.checkValue(value);
        }
    }
}
