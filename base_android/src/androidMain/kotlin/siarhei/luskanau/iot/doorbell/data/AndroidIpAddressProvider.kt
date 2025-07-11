package siarhei.luskanau.iot.doorbell.data

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import siarhei.luskanau.iot.doorbell.common.AppConstants
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import timber.log.Timber

class AndroidIpAddressProvider : IpAddressProvider {

    override suspend fun getIpAddressList(): Map<String, Pair<String, String>> {
        val ipAddressList = mutableMapOf<String, Pair<String, String>>()

        runCatching {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                Collections.list(networkInterface.inetAddresses)
                    .filter { inetAddress: InetAddress ->
                        !inetAddress.isLoopbackAddress && inetAddress is Inet4Address
                    }
                    .map { inetAddress: InetAddress ->
                        Pair(
                            inetAddress.hostAddress.orEmpty(),
                            AppConstants.DATE_FORMAT.format(System.currentTimeMillis())
                        )
                    }
                    .forEach { hostAddress: Pair<String, String> ->
                        ipAddressList[networkInterface.name] = hostAddress
                    }
            }
        }.onFailure {
            Timber.e(it)
        }

        return ipAddressList
    }
}
