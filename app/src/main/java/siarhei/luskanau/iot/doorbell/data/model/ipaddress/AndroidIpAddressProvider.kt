package siarhei.luskanau.iot.doorbell.data.model.ipaddress

import siarhei.luskanau.iot.doorbell.AppConstants
import timber.log.Timber
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import javax.inject.Inject

class AndroidIpAddressProvider @Inject constructor() : IpAddressProvider {

    override fun getIpAddressList(): List<Pair<String, String>> {
        val ipAddressList = mutableListOf<Pair<String, String>>()

        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                Collections.list(networkInterface.inetAddresses)
                        .filter { inetAddress: InetAddress ->
                            !inetAddress.isLoopbackAddress && inetAddress is Inet4Address
                        }
                        .map { inetAddress: InetAddress ->
                            inetAddress.hostAddress + " " + AppConstants.DATE_FORMAT.format(System.currentTimeMillis())
                        }
                        .forEach { hostAddress: String ->
                            ipAddressList.add(Pair(networkInterface.name, hostAddress))
                        }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return ipAddressList
    }
}