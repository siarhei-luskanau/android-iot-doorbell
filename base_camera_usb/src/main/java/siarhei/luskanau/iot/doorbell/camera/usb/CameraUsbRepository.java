package siarhei.luskanau.iot.doorbell.camera.usb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.widget.Toast;

import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener;
import com.serenegiant.usb.USBMonitor.UsbControlBlock;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

public class CameraUsbRepository implements TakePictureRepository {

    private static final String TAG = CameraUsbRepository.class.getSimpleName();

    private final Context context;

    private final OnDeviceConnectListener mOnDeviceConnectListener
            = new OnDeviceConnectListener() {

        @Override
        public void onAttach(final UsbDevice device) {
            Toast.makeText(context, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnect(final UsbDevice device,
                              final UsbControlBlock ctrlBlock, final boolean createNew) {
        }

        @Override
        public void onDisconnect(final UsbDevice device,
                                 final UsbControlBlock ctrlBlock) {

        }

        @Override
        public void onDettach(final UsbDevice device) {
            Toast.makeText(context, "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(final UsbDevice device) {
        }
    };

    public CameraUsbRepository(final Context context) {
        this.context = context;
        USBMonitor mUSBMonitor = new USBMonitor(context, mOnDeviceConnectListener);
    }

    @Override
    public Observable<byte[]> takePicture(final String cameraId) {
        return Observable.empty();
    }

}
