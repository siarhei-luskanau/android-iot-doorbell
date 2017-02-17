package siarhei.luskanau.iot.doorbell.camera;

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
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

import static android.content.Context.CAMERA_SERVICE;

public class CameraRepository implements TakePictureRepository {

    private static final String TAG = CameraRepository.class.getSimpleName();
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private static final int MAX_IMAGES = 1;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private final Context context;
    private final ImageCompressor imageCompressor;

    public CameraRepository(Context context, ImageCompressor imageCompressor) {
        this.context = context;
        this.imageCompressor = imageCompressor;
    }

    @Override
    public Observable<byte[]> takePicture() {
        List<Observable<byte[]>> observableList = new ArrayList<>();

        CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        String[] cameraIdList = {};
        try {
            cameraIdList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.d(TAG, "Cam access exception getting IDs", e);
            return Observable.error(e);
        }

        if (cameraIdList.length < 1) {
            Log.d(TAG, "No cameras found");
            return Observable.empty();
        }

        for (String cameraId : cameraIdList) {
            Observable<byte[]> observable = createCameraObservable(cameraManager, cameraId);
            observableList.add(imageCompressor.scale(observable, IMAGE_WIDTH));
        }

        return Observable.concat(observableList);
    }

    private Observable<byte[]> createCameraObservable(CameraManager cameraManager, String cameraId) {
        return Observable.create(emitter -> {
            Log.d(TAG, "Using camera id " + cameraId);

            // Initialize the image processor
            ImageReader imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES);

            HandlerThread backgroundThread = new HandlerThread("CameraBackground");
            backgroundThread.start();
            Handler backgroundHandler = new Handler(backgroundThread.getLooper());

            ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    // get image bytes
                    ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
                    final byte[] imageBytes = new byte[imageBuf.remaining()];
                    imageBuf.get(imageBytes);
                    image.close();

                    emitter.onNext(imageBytes);
                    emitter.onComplete();
                }
            };
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);

            CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice cameraDevice) {
                    Log.d(TAG, "Opened camera.");
                    // Here, we create a CameraCaptureSession for capturing still images.
                    try {
                        CameraCaptureSession.StateCallback sessionCallback =
                                new CameraCaptureSession.StateCallback() {
                                    @Override
                                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                                        // The camera is already closed
                                        if (cameraDevice == null) {
                                            return;
                                        }

                                        // When the session is ready, we start capture.
                                        try {
                                            final CaptureRequest.Builder captureBuilder =
                                                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                                            captureBuilder.addTarget(imageReader.getSurface());

                                            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                                            int rotation = display.getRotation();
                                            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                                            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                                            Log.d(TAG, "Session initialized.");

                                            CameraCaptureSession.CaptureCallback mCaptureCallback =
                                                    new CameraCaptureSession.CaptureCallback() {

                                                        @Override
                                                        public void onCaptureProgressed(CameraCaptureSession session,
                                                                                        CaptureRequest request,
                                                                                        CaptureResult partialResult) {
                                                            Log.d(TAG, "Partial result");
                                                        }

                                                        @Override
                                                        public void onCaptureCompleted(CameraCaptureSession session,
                                                                                       CaptureRequest request,
                                                                                       TotalCaptureResult result) {
                                                            if (session != null) {
                                                                session.close();
                                                                Log.d(TAG, "CaptureSession closed");
                                                            }
                                                        }
                                                    };

                                            cameraCaptureSession.capture(captureBuilder.build(), mCaptureCallback, null);
                                        } catch (CameraAccessException cae) {
                                            Log.d(TAG, "camera capture exception");
                                        }
                                    }

                                    @Override
                                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                                        Log.w(TAG, "Failed to configure camera");
                                    }
                                };

                        cameraDevice.createCaptureSession(
                                Collections.singletonList(imageReader.getSurface()),
                                sessionCallback,
                                null);
                    } catch (CameraAccessException cae) {
                        Log.d(TAG, "access exception while preparing pic", cae);
                    }
                }

                @Override
                public void onDisconnected(CameraDevice cameraDevice) {
                    Log.d(TAG, "Camera disconnected, closing.");
                    cameraDevice.close();
                }

                @Override
                public void onError(CameraDevice cameraDevice, int i) {
                    Log.d(TAG, "Camera device error, closing.");
                    emitter.onError(new RuntimeException("CameraDevice:StateCallback:onError " + i));
                    cameraDevice.close();
                }

                @Override
                public void onClosed(CameraDevice cameraDevice) {
                    Log.d(TAG, "Closed camera, releasing");
                }
            };

            // Open the camera resource
            try {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
            } catch (CameraAccessException cae) {
                Log.d(TAG, "Camera access exception", cae);
                emitter.onError(cae);
            }
        });
    }

    public static Map<String, Object> getCameraInfo(Context context) {
        Map<String, Object> map = new HashMap<>();
        try {
            CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
            String[] cameraIdList = cameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                Map<String, Object> cameraMap = new HashMap<>();
                map.put(cameraId, cameraMap);
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                cameraMap.put("CONTROL_AVAILABLE_EFFECTS", characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS));

                StreamConfigurationMap streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Map<String, Object> outputFormatsMap = new HashMap<>();
                cameraMap.put("OutputFormats", outputFormatsMap);
                int[] outputFormats = streamConfigurationMap.getOutputFormats();
                for (int outputFormat : outputFormats) {
                    StringBuilder builder = new StringBuilder();
                    for (Size size : streamConfigurationMap.getOutputSizes(outputFormat)) {
                        if (builder.length() > 0) {
                            builder.append(", ");
                        }
                        builder.append(size.toString());
                    }
                    outputFormatsMap.put(String.valueOf(outputFormat), builder.toString());
                }
            }
        } catch (CameraAccessException e) {
            Log.d(TAG, e.getMessage(), e);
        }
        return map;
    }
}