package siarhei.luskanau.iot.doorbell.repository;

import io.reactivex.Observable;

public interface ImageRepository {

    Observable<Void> sendImage(final byte[] imageBytes);
}
