package siarhei.luskanau.iot.doorbell.common

import java.io.Serializable

interface DeviceInfoProvider {

    fun buildDoorbellId(): String

    fun buildDeviceName(): String

    fun buildDeviceInfo(): Map<String, Serializable>
}
