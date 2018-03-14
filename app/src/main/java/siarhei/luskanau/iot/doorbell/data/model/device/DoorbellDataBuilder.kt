package siarhei.luskanau.iot.doorbell.data.model.device

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface DoorbellDataBuilder {

    fun buildDoorbellData(): DoorbellData

}