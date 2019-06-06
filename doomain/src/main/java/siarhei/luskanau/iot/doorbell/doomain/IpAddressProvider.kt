package siarhei.luskanau.iot.doorbell.doomain

interface IpAddressProvider {

    suspend fun getIpAddressList(): Map<String, String>
}