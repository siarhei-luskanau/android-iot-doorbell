package siarhei.luskanau.iot.doorbell.camera.usb;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.usb.UsbDevice;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener;
import com.serenegiant.usb.USBMonitor.UsbControlBlock;
import com.serenegiant.usb.UVCCamera;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

public class CameraUsbRepository implements TakePictureRepository {

    private static final String TAG = CameraUsbRepository.class.getSimpleName();
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private static final int MAX_IMAGES = 1;

    private final Context context;

    public CameraUsbRepository(final Context context) {
        this.context = context;
    }

    @Override
    public Observable<byte[]> takePicture(final String cameraId) {
        return Observable.create(emitter -> {
            try {
                final OnDeviceConnectListener onDeviceConnectListener = new OnDeviceConnectListener() {

                    @Override
                    public void onAttach(final UsbDevice usbDevice) {
                        Toast.makeText(context, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDettach(final UsbDevice usbDevice) {
                        Toast.makeText(context, "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnect(final UsbDevice usbDevice, final UsbControlBlock usbControlBlock, final boolean createNew) {
                        openDevice(emitter, usbControlBlock);
                    }

                    @Override
                    public void onDisconnect(final UsbDevice usbDevice, final UsbControlBlock usbControlBlock) {
                    }

                    @Override
                    public void onCancel(final UsbDevice usbDevice) {
                    }
                };

                final USBMonitor usbMonitor = new USBMonitor(context, onDeviceConnectListener);
                usbMonitor.register();

                final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(context, com.serenegiant.uvccamera.R.xml.device_filter);
                final List<UsbDevice> usbDevices = usbMonitor.getDeviceList(filter);
                if (usbDevices != null) {
                    for (final UsbDevice usbDevice : usbDevices) {
                        Log.d(TAG, String.valueOf(usbDevice));
                        usbMonitor.requestPermission(usbDevice);
                    }
                }
            } catch (final Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
        });
    }

    @SuppressWarnings({"MethodOnlyUsedFromInnerClass", "TypeMayBeWeakened"})
    private void openDevice(final ObservableEmitter<byte[]> emitter, final UsbControlBlock usbControlBlock) {
        try {
            final UVCCamera uvcCamera = new UVCCamera();
            uvcCamera.open(usbControlBlock);

            final HandlerThread backgroundThread = new HandlerThread("CameraBackground");
            backgroundThread.start();
            final Handler backgroundHandler = new Handler(backgroundThread.getLooper());

            final ImageReader.OnImageAvailableListener onImageAvailableListener = reader -> {
                try {
                    Log.d(TAG, "ImageReader.OnImageAvailableListener.onImageAvailable");
                    final Image image = reader.acquireLatestImage();
//                    try {
//                        // get image bytes
//                        final Plane[] planes = image.getPlanes();
//                        if (planes != null && planes.length > 0) {
//                            final ByteBuffer imageBuf = planes[0].getBuffer();
//                            final byte[] imageBytes = new byte[imageBuf.remaining()];
//                            imageBuf.get(imageBytes);
//                            image.close();
//
//                            emitter.onNext(imageBytes);
//                            uvcCamera.stopPreview();
//                        }
//                    } catch (final Throwable e) {
//                        Log.d(TAG, e.getMessage(), e);
//                    }
                } catch (final Throwable e) {
                    //emitter.onError(e);
                    emitter.onComplete();
                    Log.d(TAG, e.getMessage(), e);
                }
            };

            final ImageReader imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES);
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);

            uvcCamera.setPreviewDisplay(imageReader.getSurface());
            uvcCamera.startPreview();
        } catch (final Exception e) {
            emitter.onError(e);
            Log.d(TAG, e.getMessage(), e);
        }
    }
}
