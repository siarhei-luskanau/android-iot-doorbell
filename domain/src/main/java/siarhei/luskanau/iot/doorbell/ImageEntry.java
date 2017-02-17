package siarhei.luskanau.iot.doorbell;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageEntry {

    @SerializedName(DomainConstants.IMAGE_ID)
    private String imageId;
    @SerializedName(DomainConstants.TIMESTAMP)
    private Long timestamp;
    @SerializedName(DomainConstants.IMAGE)
    private String image;
    @SerializedName(DomainConstants.ANNOTATIONS)
    private List<String> annotations;
    @SerializedName(DomainConstants.IMAGE_LENGTH)
    private long imageLength;

    public String getImageId() {
        return imageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public long getImageLength() {
        return imageLength;
    }
}
