package siarhei.luskanau.iot.doorbell.doomain

import java.io.Serializable

interface DeviceInfoProvider {

    fun buildDeviceId(): String

    fun buildDeviceName(): String

    fun isAndroidThings(): Boolean

    fun buildDeviceInfo(): Map<String, Serializable>
}