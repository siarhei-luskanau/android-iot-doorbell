package siarhei.luskanau.iot.doorbell.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.impl.CameraInfoInternal
import androidx.core.content.getSystemService
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.CameraInfoData
import siarhei.luskanau.iot.doorbell.data.model.CameraxInfoData
import siarhei.luskanau.iot.doorbell.data.model.SizeData
import timber.log.Timber

abstract class BaseCameraRepository(
    private val context: Context
) : CameraRepository {

    override suspend fun getCamerasList(): List<CameraData> =
        mutableListOf<CameraData>().also { list ->
            runCatching {
                context.getSystemService<CameraManager>()?.let { cameraManager ->
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
                        sizes.putAll(
                            configs.getOutputSizes(ImageFormat.JPEG)
                                .associateBy(
                                    { size: Size -> size.height * size.width },
                                    { size: Size -> size }
                                )
                        )
                    }
            }.onFailure {
                Timber.e(it)
            }
        }

    private fun getInfo(cameraManager: CameraManager, cameraId: String): CameraInfoData =
        runCatching {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            CameraInfoData(
                lensFacing = getLensFacingName(
                    characteristics.get(CameraCharacteristics.LENS_FACING)
                ),
                infoSupportedHardwareLevel = getHardwareLevelName(
                    characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                ),
                scalerStreamConfigurationMap = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?.let { configs: StreamConfigurationMap ->
                        configs.outputFormats
                            .associateBy(
                                { outputFormat: Int ->
                                    outputFormatName[outputFormat] ?: "$outputFormat:$outputFormat"
                                },
                                { outputFormat: Int ->
                                    configs.getOutputSizes(outputFormat)
                                        .associateBy(
                                            { it.toString() },
                                            { it.toString() }
                                        )
                                }
                            )
                    },
                controlAvailableEffects = characteristics
                    .get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)
                    ?.associateBy(
                        { effect: Int -> getEffectName(effect) },
                        { effect: Int -> effect.toString() }
                    ),
            )
        }.getOrElse {
            Timber.d("Cam access exception getting characteristics.")
            CameraInfoData(error = it.toString())
        }

    @SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
    private fun getCameraxInfo(cameraId: String): CameraxInfoData =
        runCatching {
            val cameraSelector = CameraSelector.Builder()
                .addCameraFilter { cameras ->
                    cameras.filter { cameraInfo ->
                        val cameraInfoInternal = cameraInfo as CameraInfoInternal
                        cameraInfoInternal.cameraId == cameraId
                    }
                }
                .build()
            CameraX.getCameraWithCameraSelector(cameraSelector).let { cameraInternal ->
                cameraInternal.cameraInfo.implementationType
                cameraInternal.cameraInfo.sensorRotationDegrees.toString()
                cameraInternal.cameraInfo.hasFlashUnit().toString()
            }
            CameraxInfoData()
        }.getOrElse {
            Timber.d("Cam access exception getting characteristics.")
            CameraxInfoData(
                error = it.toString()
            )
        }

    private fun getLensFacingName(lensFacing: Int?): String =
        when (lensFacing) {
            CameraMetadata.LENS_FACING_FRONT -> "LENS_FACING_FRONT"
            CameraMetadata.LENS_FACING_BACK -> "LENS_FACING_BACK"
            CameraMetadata.LENS_FACING_EXTERNAL -> "LENS_FACING_EXTERNAL"
            else -> lensFacing.toString()
        } + ":$lensFacing"

    private fun getHardwareLevelName(hardwareLevel: Int?): String =
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

    companion object {
        private val outputFormatName = mapOf(
            ImageFormat.UNKNOWN to "UNKNOWN",
            ImageFormat.JPEG to "JPEG",
            ImageFormat.YV12 to "YV12",
            ImageFormat.YUY2 to "YUY2",
            ImageFormat.DEPTH16 to "DEPTH16",
            ImageFormat.DEPTH_POINT_CLOUD to "DEPTH_POINT_CLOUD",
            ImageFormat.FLEX_RGBA_8888 to "FLEX_RGBA_8888",
            ImageFormat.FLEX_RGB_888 to "FLEX_RGB_888",
            ImageFormat.NV16 to "NV16",
            ImageFormat.NV21 to "NV21",
            ImageFormat.PRIVATE to "PRIVATE",
            ImageFormat.RAW10 to "RAW10",
            ImageFormat.RAW12 to "RAW12",
            ImageFormat.RAW_SENSOR to "RAW_SENSOR",
            ImageFormat.RGB_565 to "RGB_565",
            ImageFormat.YUV_420_888 to "YUV_420_888",
            ImageFormat.YUV_422_888 to "YUV_422_888",
            ImageFormat.YUV_444_888 to "YUV_444_888"
        )
    }
}
