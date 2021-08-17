package siarhei.luskanau.iot.doorbell.common

interface DeviceInfoProvider {

    fun buildDoorbellId(): String

    fun buildDeviceName(): String

    fun buildDeviceInfo(): Map<String, String>
}
