package siarhei.luskanau.iot.doorbell;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.Map;

public class DeviceInfo {

    private final String deviceId;
    private String name;
    private final String buildDevice = Build.DEVICE;
    private final String buildModel = Build.MODEL;
    private final int buildVersionSdkInt = Build.VERSION.SDK_INT;
    private final String buildVersionRelease = Build.VERSION.RELEASE;

    private final Map<String, Object> additionalInfo;

    public DeviceInfo(final Context context) {
        this(context, null);
    }

    public DeviceInfo(final Context context, final Map<String, Object> additionalInfo) {
        deviceId = getDeviceId(context);
        this.additionalInfo = additionalInfo;
    }

    private String getDeviceId(final Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getBuildDevice() {
        return buildDevice;
    }

    public String getBuildModel() {
        return buildModel;
    }

    public int getBuildVersionSdkInt() {
        return buildVersionSdkInt;
    }

    public String getBuildVersionRelease() {
        return buildVersionRelease;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
}
