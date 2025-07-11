package siarhei.luskanau.iot.doorbell.common

import androidx.paging.Pager
import androidx.paging.PagingConfig
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImagesDataSourceFactoryImpl(private val doorbellRepository: DoorbellRepository) :
    ImagesDataSourceFactory {

    override fun createPager(doorbellId: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            ImagesDataSourceImpl(
                doorbellRepository = doorbellRepository,
                doorbellId = doorbellId
            )
        }
    )

    companion object {
        private const val PAGE_SIZE = 20
    }
}
