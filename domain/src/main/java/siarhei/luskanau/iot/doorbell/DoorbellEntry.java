package siarhei.luskanau.iot.doorbell;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DoorbellEntry {

    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("name")
    private String name;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("image")
    private String image;
    @SerializedName("annotations")
    private Map<String, Float> annotations;

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }

    public Map<String, Float> getAnnotations() {
        return annotations;
    }
}