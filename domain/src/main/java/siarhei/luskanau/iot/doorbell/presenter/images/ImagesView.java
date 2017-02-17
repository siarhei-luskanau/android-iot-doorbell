package siarhei.luskanau.iot.doorbell.presenter.images;

import java.util.List;

import siarhei.luskanau.iot.doorbell.ImageEntry;

public interface ImagesView {

    void onImageListUpdated(List<ImageEntry> list);

    void showErrorMessage(CharSequence errorMessage);
}
