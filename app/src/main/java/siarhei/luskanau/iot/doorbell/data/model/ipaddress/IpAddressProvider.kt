package siarhei.luskanau.iot.doorbell.data.model.ipaddress

interface IpAddressProvider {

    fun getIpAddressList(): List<Pair<String, String>>
}