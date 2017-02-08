package siarhei.luskanau.iot.doorbell.repository;

import android.support.v4.util.Pair;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.DeviceInfo;

public interface ImageRepository {

    Observable<Void> saveImage(String deviceId, byte[] imageBytes);

    Observable<Void> sendDeviceInfo(DeviceInfo deviceInfo);

    Observable<Void> sendDeviceIpAddress(String deviceId, Pair<String, String> ipAddress);
}
