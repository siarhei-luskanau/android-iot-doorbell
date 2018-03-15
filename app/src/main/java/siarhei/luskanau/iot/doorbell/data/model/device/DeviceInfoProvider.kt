package siarhei.luskanau.iot.doorbell.data.model.device

interface DeviceInfoProvider {

    fun buildDeviceId(): String

    fun buildDeviceName(): String

    fun buildDeviceInfo(): Map<String, Any>

}