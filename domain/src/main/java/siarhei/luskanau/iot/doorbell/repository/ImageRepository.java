package siarhei.luskanau.iot.doorbell.repository;

import android.support.v4.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;

public interface ImageRepository {

    Observable<Void> saveImage(String deviceId, byte[] imageBytes);

    Observable<Void> sendDeviceInfo(DeviceInfo deviceInfo);

    Observable<DoorbellEntry> listenDoorbellEntry(String deviceId);

    Observable<List<DoorbellEntry>> listenDoorbellEntryList();

    Observable<Void> sendDeviceIpAddress(String deviceId, Pair<String, String> ipAddress);
}
