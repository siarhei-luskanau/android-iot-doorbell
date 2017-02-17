package siarhei.luskanau.iot.doorbell;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DoorbellEntry {

    @SerializedName(DomainConstants.DEVICE_ID)
    private String deviceId;
    @SerializedName(DomainConstants.NAME)
    private String name;
    @SerializedName(DomainConstants.RING)
    private Boolean ring;
    @SerializedName(DomainConstants.IMAGES)
    private Map<String, ImageEntry> images;

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Boolean getRing() {
        return ring;
    }

    public Map<String, ImageEntry> getImages() {
        return images;
    }
}