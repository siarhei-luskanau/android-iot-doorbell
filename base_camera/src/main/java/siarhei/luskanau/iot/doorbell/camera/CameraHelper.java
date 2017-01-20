package siarhei.luskanau.iot.doorbell.camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

import com.google.gson.Gson;

public class CameraHelper {

    private static final String TAG = CameraHelper.class.getSimpleName();

    private Context context;
    private Gson gson = new Gson();

    public CameraHelper(Context context) {
        this.context = context;
    }

    public void cameraInfo() {
        try {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIdList = cameraManager.getCameraIdList();
            Log.i(TAG, "CameraIdList: " + gson.toJson(cameraIdList));

            for (String cameraId : cameraIdList) {
                Log.d(TAG, "Using camera id " + cameraId);
                try {
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                    StreamConfigurationMap configs = characteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    int[] outputFormats = configs.getOutputFormats();
                    Log.i(TAG, "OutputFormats: " + gson.toJson(outputFormats));
                    for (int format : outputFormats) {
                        Log.i(TAG, "Getting sizes for format " + format + ": "
                                + gson.toJson(configs.getOutputSizes(format)));
                    }

                    int[] effects = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
                    Log.i(TAG, "effects: " + gson.toJson(effects));
                } catch (CameraAccessException e) {
                    Log.d(TAG, "Cam access exception getting characteristics.");
                }
            }
        } catch (CameraAccessException e) {
            Log.d(TAG, "Camera access exception getting IDs");
        }
    }
}
