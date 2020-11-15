package siarhei.luskanau.iot.doorbell.common

import java.io.Serializable

interface DeviceInfoProvider {

    fun buildDoorbellId(): String

    fun buildDeviceName(): String

    fun isAndroidThings(): Boolean

    fun buildDeviceInfo(): Map<String, Serializable>
}
