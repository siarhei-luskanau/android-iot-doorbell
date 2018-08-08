package siarhei.luskanau.iot.doorbell.data.model.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import timber.log.Timber
import javax.inject.Inject

class AndroidCameraDataProvider @Inject constructor(private val context: Context) : CameraDataProvider {

    override fun getCamerasList(): List<CameraData> {
        val list = mutableListOf<CameraData>()

        try {
            (context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?)?.let { cameraManager ->
                cameraManager.cameraIdList
                        .map { cameraId: String ->
                            val sizes = mutableMapOf<Int, Size>()
                            try {
                                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.let { configs: StreamConfigurationMap ->
                                    sizes.putAll(configs.getOutputSizes(ImageFormat.JPEG).associate { Pair(it.height * it.width, it) })
                                }
                            } catch (t: Throwable) {
                                Timber.e(t)
                            }

                            val info = mutableMapOf<String, Any>()
                            try {
                                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                                info["LENS_FACING"] = getLensFacingName(characteristics.get(CameraCharacteristics.LENS_FACING))

                                info["INFO_SUPPORTED_HARDWARE_LEVEL"] = getHardwareLevelName(characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))

                                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.let { configs: StreamConfigurationMap ->
                                    info["SCALER_STREAM_CONFIGURATION_MAP"] = configs.outputFormats
                                            .associate { outputFormat: Int ->
                                                Pair(
                                                        getOtputFormaName(outputFormat) + ":$outputFormat",
                                                        configs.getOutputSizes(outputFormat)
                                                                .associate { size: Size? ->
                                                                    Pair("size: $size", size)
                                                                }
                                                )
                                            }
                                }

                                info["CONTROL_AVAILABLE_EFFECTS"] = characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)
                                        ?.associate { effect: Int ->
                                            Pair(getEffectName(effect), effect)
                                        } ?: emptyArray<Pair<String, String>>()

                            } catch (e: Throwable) {
                                info["error"] = e.message as Any
                                Timber.d("Cam access exception getting characteristics.")
                            }

                            val maxSize: Size? = sizes.values.sortedWith(
                                    compareBy { size -> size.height * size.width }
                            ).last()
                            val name: String? = maxSize?.let {
                                cameraId + ":" + it.toString()
                            }

                            list.add(CameraData(cameraId, name, sizes, info))
                        }
            }
        } catch (t: Throwable) {
            Timber.e(t)
        }

        return list
    }

    private fun getLensFacingName(lensFacing: Int?): Any =
            when (lensFacing) {
                CameraMetadata.LENS_FACING_FRONT -> "LENS_FACING_FRONT"
                CameraMetadata.LENS_FACING_BACK -> "LENS_FACING_BACK"
                CameraMetadata.LENS_FACING_EXTERNAL -> "LENS_FACING_EXTERNAL"
                else -> lensFacing.toString()
            } + ":$lensFacing"

    private fun getHardwareLevelName(hardwareLevel: Int?): Any =
            when (hardwareLevel) {
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 -> "INFO_SUPPORTED_HARDWARE_LEVEL_3"
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> "INFO_SUPPORTED_HARDWARE_LEVEL_FULL"
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY -> "INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY"
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED -> "INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED"
                else -> hardwareLevel.toString()
            } + ":$hardwareLevel"

    private fun getOtputFormaName(outputFormat: Int): String =
            when (outputFormat) {
                ImageFormat.UNKNOWN -> "UNKNOWN"
                ImageFormat.JPEG -> "JPEG"
                ImageFormat.YV12 -> "YV12"
                ImageFormat.YUY2 -> "YUY2"
                ImageFormat.DEPTH16 -> "DEPTH16"
                ImageFormat.DEPTH_POINT_CLOUD -> "DEPTH_POINT_CLOUD"
                ImageFormat.FLEX_RGBA_8888 -> "FLEX_RGBA_8888"
                ImageFormat.FLEX_RGB_888 -> "FLEX_RGB_888"
                ImageFormat.NV16 -> "NV16"
                ImageFormat.NV21 -> "NV21"
                ImageFormat.PRIVATE -> "PRIVATE"
                ImageFormat.RAW10 -> "RAW10"
                ImageFormat.RAW12 -> "RAW12"
                ImageFormat.RAW_PRIVATE -> "RAW_PRIVATE"
                ImageFormat.RAW_SENSOR -> "RAW_SENSOR"
                ImageFormat.RGB_565 -> "RGB_565"
                ImageFormat.YUV_420_888 -> "YUV_420_888"
                ImageFormat.YUV_422_888 -> "YUV_422_888"
                ImageFormat.YUV_444_888 -> "YUV_444_888"
                else -> outputFormat.toString()
            }

    private fun getEffectName(effect: Int): String =
            when (effect) {
                CameraMetadata.CONTROL_EFFECT_MODE_OFF -> "CONTROL_EFFECT_MODE_OFF"
                CameraMetadata.CONTROL_EFFECT_MODE_MONO -> "CONTROL_EFFECT_MODE_MONO"
                CameraMetadata.CONTROL_EFFECT_MODE_NEGATIVE -> "CONTROL_EFFECT_MODE_NEGATIVE"
                CameraMetadata.CONTROL_EFFECT_MODE_SOLARIZE -> "CONTROL_EFFECT_MODE_SOLARIZE"
                CameraMetadata.CONTROL_EFFECT_MODE_SEPIA -> "CONTROL_EFFECT_MODE_SEPIA"
                CameraMetadata.CONTROL_EFFECT_MODE_POSTERIZE -> "CONTROL_EFFECT_MODE_POSTERIZE"
                CameraMetadata.CONTROL_EFFECT_MODE_WHITEBOARD -> "CONTROL_EFFECT_MODE_WHITEBOARD"
                CameraMetadata.CONTROL_EFFECT_MODE_BLACKBOARD -> "CONTROL_EFFECT_MODE_BLACKBOARD"
                CameraMetadata.CONTROL_EFFECT_MODE_AQUA -> "CONTROL_EFFECT_MODE_AQUA"
                else -> effect.toString()
            }

}