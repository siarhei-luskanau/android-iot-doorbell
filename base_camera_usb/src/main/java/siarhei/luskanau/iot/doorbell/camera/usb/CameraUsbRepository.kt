package siarhei.luskanau.iot.doorbell.camera.usb

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.usb.UsbDevice
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import com.serenegiant.usb.DeviceFilter
import com.serenegiant.usb.USBMonitor
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener
import com.serenegiant.usb.USBMonitor.UsbControlBlock
import com.serenegiant.usb.UVCCamera
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository
import timber.log.Timber

class CameraUsbRepository(private val context: Context) : TakePictureRepository {

    override fun takePicture(cameraId: String): Observable<ByteArray> {
        return Observable.create { emitter ->
            try {
                val onDeviceConnectListener = object : OnDeviceConnectListener {

                    override fun onAttach(usbDevice: UsbDevice) {
                        Toast.makeText(context, "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDettach(usbDevice: UsbDevice) {
                        Toast.makeText(context, "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show()
                    }

                    override fun onConnect(usbDevice: UsbDevice, usbControlBlock: UsbControlBlock, createNew: Boolean) {
                        openDevice(emitter, usbControlBlock)
                    }

                    override fun onDisconnect(usbDevice: UsbDevice, usbControlBlock: UsbControlBlock) {}

                    override fun onCancel(usbDevice: UsbDevice) {}
                }

                val usbMonitor = USBMonitor(context, onDeviceConnectListener)
                usbMonitor.register()

                val filter = DeviceFilter.getDeviceFilters(context, com.serenegiant.uvccamera.R.xml.device_filter)
                val usbDevices = usbMonitor.getDeviceList(filter)
                if (usbDevices != null) {
                    for (usbDevice in usbDevices) {
                        Timber.d(usbDevice.toString())
                        usbMonitor.requestPermission(usbDevice)
                    }
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    private fun openDevice(emitter: ObservableEmitter<ByteArray>, usbControlBlock: UsbControlBlock) {
        try {
            val uvcCamera = UVCCamera()
            uvcCamera.open(usbControlBlock)

            val backgroundThread = HandlerThread("CameraBackground")
            backgroundThread.start()
            val backgroundHandler = Handler(backgroundThread.looper)

            val onImageAvailableListener = ImageReader.OnImageAvailableListener { reader: ImageReader ->
                try {
                    Timber.d("ImageReader.OnImageAvailableListener.onImageAvailable")
                    val image = reader.acquireLatestImage()
                    try {
                        // get image bytes
                        val planes = image.getPlanes()
                        if (planes != null && planes!!.size > 0) {
                            val imageBuf = planes!![0].getBuffer()
                            val imageBytes = ByteArray(imageBuf.remaining())
                            imageBuf.get(imageBytes)
                            image.close()

                            emitter.onNext(imageBytes)
                            uvcCamera.stopPreview()
                        }
                    } catch (e: Throwable) {
                        Timber.d(e)
                    }

                } catch (e: Throwable) {
                    //emitter.onError(e);
                    emitter.onComplete()
                    Timber.d(e)
                }
            }

            val imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, MAX_IMAGES)
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)

            uvcCamera.setPreviewDisplay(imageReader.surface)
            uvcCamera.startPreview()
        } catch (e: Exception) {
            emitter.onError(e)
            Timber.d(e)
        }

    }

    companion object {

        private val IMAGE_WIDTH = 320
        private val IMAGE_HEIGHT = 240
        private val MAX_IMAGES = 1
    }
}
