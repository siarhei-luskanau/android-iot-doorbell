package siarhei.luskanau.iot.doorbell.common

interface IpAddressProvider {

    suspend fun getIpAddressList(): Map<String, Pair<String, String>>
}
