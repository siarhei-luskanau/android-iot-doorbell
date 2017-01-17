package siarhei.luskanau.iot.doorbell.domain;

import io.reactivex.Observable;

public interface ImageRepository {

    Observable<Void> sendImage(final byte[] imageBytes);
}
