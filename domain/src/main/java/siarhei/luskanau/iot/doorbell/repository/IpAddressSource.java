package siarhei.luskanau.iot.doorbell.repository;

import android.support.v4.util.Pair;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import timber.log.Timber;

public class IpAddressSource {

    public Observable<Pair<String, String>> listenIpAddress() {
        return Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(aLong -> {
                    final Collection<Pair<String, String>> ipAddressList = new ArrayList<>();
                    try {
                        for (final NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                            for (final InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                    final String ipAddress = inetAddress.getHostAddress() +
                                            " " + new Date();
                                    ipAddressList.add(new Pair<>(networkInterface.getName(), ipAddress));
                                }
                            }
                        }
                    } catch (final Exception e) {
                        Timber.e(e);
                    }
                    return Observable.fromIterable(ipAddressList);
                });
    }
}
