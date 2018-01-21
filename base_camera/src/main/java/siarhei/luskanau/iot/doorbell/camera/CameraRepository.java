package siarhei.luskanau.iot.doorbell.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;
import timber.log.Timber;

import static android.content.Context.CAMERA_SERVICE;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CameraRepository implements TakePictureRepository {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private static final int MAX_IMAGES = 1;

    static {
        //noinspection MagicNumber
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        //noinspection MagicNumber
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        //noinspection MagicNumber
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        //noinspection MagicNumber
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private final Context context;
    private final ImageCompressor imageCompressor;

    public CameraRepository(final Context context, final ImageCompressor imageCompressor) {
        this.context = context;
        this.imageCompressor = imageCompressor;
    }

    @Override
    public Observable<byte[]> takePicture(final String cameraId) {
        final CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        final Observable<byte[]> observable = createCameraObservable(cameraManager, cameraId);
        return imageCompressor.scale(observable, IMAGE_WIDTH);
    }

    @SuppressLint("MissingPermission")
    private Observable<byte[]> createCameraObservable(final CameraManager cameraManager, final String cameraId) {
        return Observable.create(emitter -> {
            Timber.d("Using camera id %s", cameraId);

            // Initialize the image processor
            final ImageReader imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES);

            final HandlerThread backgroundThread = new HandlerThread("CameraBackground:" + cameraId);
            backgroundThread.start();
            final Handler backgroundHandler = new Handler(backgroundThread.getLooper());

            final ImageReader.OnImageAvailableListener onImageAvailableListener = reader -> {
                final Image image = reader.acquireLatestImage();
                // get image bytes
                final ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
                final byte[] imageBytes = new byte[imageBuf.remaining()];
                imageBuf.get(imageBytes);
                image.close();

                emitter.onNext(imageBytes);
                emitter.onComplete();
            };
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);

            final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

                @Override
                public void onOpened(@NonNull final CameraDevice cameraDevice) {
                    Timber.d("Opened camera.");
                    // Here, we create a CameraCaptureSession for capturing still images.
                    try {
                        final CameraCaptureSession.StateCallback sessionCallback =
                                new CameraCaptureSession.StateCallback() {

                                    @Override
                                    public void onConfigured(@NonNull final CameraCaptureSession cameraCaptureSession) {
                                        // When the session is ready, we start capture.
                                        try {
                                            final CaptureRequest.Builder captureBuilder =
                                                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                                            captureBuilder.addTarget(imageReader.getSurface());

                                            final WindowManager windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
                                            if (windowManager != null) {
                                                final Display display = windowManager.getDefaultDisplay();
                                                final int rotation = display.getRotation();
                                                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                                            }

                                            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                                            Timber.d("Session initialized.");

                                            final CameraCaptureSession.CaptureCallback mCaptureCallback =
                                                    new CameraCaptureSession.CaptureCallback() {

                                                        @Override
                                                        public void onCaptureProgressed(@NonNull final CameraCaptureSession session,
                                                                                        @NonNull final CaptureRequest request,
                                                                                        @NonNull final CaptureResult partialResult) {
                                                            Timber.d("Partial result");
                                                        }

                                                        @Override
                                                        public void onCaptureCompleted(@NonNull final CameraCaptureSession session,
                                                                                       @NonNull final CaptureRequest request,
                                                                                       @NonNull final TotalCaptureResult result) {
                                                            session.close();
                                                            Timber.d("CaptureSession closed");
                                                        }
                                                    };

                                            cameraCaptureSession.capture(captureBuilder.build(), mCaptureCallback, null);
                                        } catch (final CameraAccessException cae) {
                                            Timber.d("camera capture exception");
                                        }
                                    }

                                    @Override
                                    public void onConfigureFailed(@NonNull final CameraCaptureSession cameraCaptureSession) {
                                        Timber.w("Failed to configure camera");
                                    }
                                };

                        cameraDevice.createCaptureSession(
                                Collections.singletonList(imageReader.getSurface()),
                                sessionCallback,
                                null);
                    } catch (final CameraAccessException cae) {
                        Timber.d(cae, "access exception while preparing pic");
                    }
                }

                @Override
                public void onDisconnected(@NonNull final CameraDevice cameraDevice) {
                    Timber.d("Camera disconnected, closing.");
                    cameraDevice.close();
                }

                @Override
                public void onError(@NonNull final CameraDevice cameraDevice, final int i) {
                    Timber.d("Camera device error, closing.");
                    emitter.onError(new RuntimeException("CameraDevice:StateCallback:onError " + i));
                    cameraDevice.close();
                }

                @Override
                public void onClosed(@NonNull final CameraDevice cameraDevice) {
                    Timber.d("Closed camera, releasing");
                }
            };

            // Open the camera resource
            try {
                //noinspection MissingPermission
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
            } catch (final CameraAccessException cae) {
                Timber.d(cae, "Camera access exception");
                emitter.onError(cae);
            }
        });
    }

    @SuppressWarnings("unused")
    public static Map<String, Object> getCameraInfo(final Context context) {
        final Map<String, Object> map = new HashMap<>();
        try {
            final CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
            if (cameraManager != null) {
                final String[] cameraIdList = cameraManager.getCameraIdList();
                for (final String cameraId : cameraIdList) {
                    final Map<String, Object> cameraMap = new HashMap<>();
                    map.put(cameraId, cameraMap);
                    final CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                    cameraMap.put("CONTROL_AVAILABLE_EFFECTS", characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS));

                    final StreamConfigurationMap streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    final Map<String, Object> outputFormatsMap = new HashMap<>();
                    if (streamConfigurationMap != null) {
                        cameraMap.put("OutputFormats", outputFormatsMap);
                        final int[] outputFormats = streamConfigurationMap.getOutputFormats();
                        for (final int outputFormat : outputFormats) {
                            final StringBuilder builder = new StringBuilder();
                            for (final Size size : streamConfigurationMap.getOutputSizes(outputFormat)) {
                                if (builder.length() > 0) {
                                    builder.append(", ");
                                }
                                builder.append(size);
                            }
                            outputFormatsMap.put(String.valueOf(outputFormat), builder.toString());
                        }
                    }
                }
            }
        } catch (final CameraAccessException e) {
            Timber.d(e);
        }
        return map;
    }
}
