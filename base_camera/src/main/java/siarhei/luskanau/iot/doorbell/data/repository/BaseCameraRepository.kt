package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import androidx.camera.core.CameraX
import androidx.core.content.ContextCompat
import java.io.Serializable
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.SizeData
import timber.log.Timber

abstract class BaseCameraRepository(
    private val context: Context
) : CameraRepository {

    override suspend fun getCamerasList(): List<CameraData> =
        mutableListOf<CameraData>().also { list ->
            runCatching {
                (ContextCompat.getSystemService(
                    context,
                    CameraManager::class.java
                ))?.let { cameraManager ->
                    cameraManager.cameraIdList
                        .map { cameraId: String ->
                            val sizes = getSizes(cameraManager, cameraId)
                            val name: String? = sizes.values
                                .sortedWith(compareBy { size -> size.height * size.width })
                                .lastOrNull()
                                ?.let { maxSize: Size? -> maxSize?.let { "$cameraId:$it" } }
                            val info = getInfo(cameraManager, cameraId)
                            val cameraxInfo = getCameraxInfo(cameraId)

                            list.add(
                                CameraData(
                                    cameraId = cameraId,
                                    name = name.orEmpty(),
                                    sizes = sizes.mapValues { entry ->
                                        SizeData(
                                            entry.value.width,
                                            entry.value.height
                                        )
                                    },
                                    info = info,
                                    cameraxInfo = cameraxInfo
                                )
                            )
                        }
                }
            }.onFailure {
                Timber.e(it)
            }
        }

    private fun getSizes(cameraManager: CameraManager, cameraId: String): Map<Int, Size> =
        mutableMapOf<Int, Size>().also { sizes ->
            runCatching {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?.let { configs: StreamConfigurationMap ->
                        sizes.putAll(configs.getOutputSizes(ImageFormat.JPEG)
                            .associateBy(
                                { size: Size -> size.height * size.width },
                                { size: Size -> size }
                            ))
                    }
            }.onFailure {
                Timber.e(it)
            }
        }

    private fun getInfo(cameraManager: CameraManager, cameraId: String): Map<String, Serializable> =
        mutableMapOf<String, Serializable>().also { info ->
            runCatching {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                info["LENS_FACING"] = getLensFacingName(
                    characteristics.get(CameraCharacteristics.LENS_FACING)
                )

                info["INFO_SUPPORTED_HARDWARE_LEVEL"] = getHardwareLevelName(
                    characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                )

                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?.let { configs: StreamConfigurationMap ->
                        info["SCALER_STREAM_CONFIGURATION_MAP"] = configs.outputFormats
                            .associateBy(
                                { outputFormat: Int ->
                                    getOutputFormaName(outputFormat) + ":$outputFormat"
                                },
                                { outputFormat: Int ->
                                    configs.getOutputSizes(outputFormat)
                                        .associateBy(
                                            { it.toString() },
                                            { it.toString() }
                                        )
                                }
                            ) as Serializable
                    }

                info["CONTROL_AVAILABLE_EFFECTS"] =
                    characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)
                        ?.associateBy(
                            { effect: Int -> getEffectName(effect) },
                            { effect: Int -> effect }
                        ) as Serializable
            }.onFailure {
                info["error"] = it.message as Serializable
                Timber.d("Cam access exception getting characteristics.")
            }
        }

    @SuppressLint("RestrictedApi")
    private fun getCameraxInfo(cameraId: String): Map<String, Serializable> =
        mutableMapOf<String, Serializable>().also { cameraxInfo ->
            runCatching {
                CameraX.getCameraInfo(cameraId).let { cameraInfo ->
                    cameraxInfo["CameraInfo:lensFacing"] = cameraInfo.lensFacing.toString()
                    cameraxInfo["CameraInfo:sensorRotationDegrees"] =
                        cameraInfo.sensorRotationDegrees
                }
            }.onFailure {
                cameraxInfo["error"] = it.message as Serializable
                Timber.d("Cam access exception getting characteristics.")
            }
        }

    private fun getLensFacingName(lensFacing: Int?): Serializable =
        when (lensFacing) {
            CameraMetadata.LENS_FACING_FRONT -> "LENS_FACING_FRONT"
            CameraMetadata.LENS_FACING_BACK -> "LENS_FACING_BACK"
            CameraMetadata.LENS_FACING_EXTERNAL -> "LENS_FACING_EXTERNAL"
            else -> lensFacing.toString()
        } + ":$lensFacing"

    private fun getHardwareLevelName(hardwareLevel: Int?): Serializable =
        when (hardwareLevel) {
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 ->
                "INFO_SUPPORTED_HARDWARE_LEVEL_3"

            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL ->
                "INFO_SUPPORTED_HARDWARE_LEVEL_FULL"

            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY ->
                "INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY"

            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED ->
                "INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED"

            else -> hardwareLevel.toString()
        } + ":$hardwareLevel"

    private fun getOutputFormaName(outputFormat: Int): String =
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
