package siarhei.luskanau.iot.doorbell.common

import java.io.IOException
import kotlin.random.Random
import kotlinx.coroutines.delay
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class DefaultDoorbellsDataSource(
    private val doorbellRepository: DoorbellRepository
) : DoorbellsDataSource() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, DoorbellData> =
        try {
            val data = doorbellRepository.getDoorbellsList(params.loadSize, params.key)

            delay(Random.nextLong(DELAY_FROM, DELAY_UNTIL))
            if (Random.nextInt(PERCENT_FULL) > PERCENT_ERROR) {
                throw IOException("test")
            }

            LoadResult.Page(
                data = data,
                prevKey = params.key,
                nextKey = data.lastOrNull()?.doorbellId
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    companion object {
        private const val DELAY_FROM = 100L
        private const val DELAY_UNTIL = 3000L
        private const val PERCENT_FULL = 100
        private const val PERCENT_ERROR = 90
    }
}
