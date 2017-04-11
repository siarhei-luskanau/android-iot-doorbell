package siarhei.luskanau.iot.doorbell.repository;

import io.reactivex.Observable;

public interface TakePictureRepository {

    Observable<byte[]> takePicture(String cameraId);
}
