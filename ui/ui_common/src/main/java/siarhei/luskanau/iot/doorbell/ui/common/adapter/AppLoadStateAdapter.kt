package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class AppLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<AppLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: AppLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): AppLoadStateViewHolder {
        return AppLoadStateViewHolder.create(parent, retry)
    }
}
