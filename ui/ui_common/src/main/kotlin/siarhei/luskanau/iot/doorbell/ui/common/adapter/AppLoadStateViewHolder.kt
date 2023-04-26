package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import siarhei.luskanau.iot.doorbell.ui.common.R
import siarhei.luskanau.iot.doorbell.ui.common.databinding.ViewItemLoadStateFooterBinding

class AppLoadStateViewHolder(
    private val binding: ViewItemLoadStateFooterBinding,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): AppLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_load_state_footer, parent, false)
            val binding = ViewItemLoadStateFooterBinding.bind(view)
            return AppLoadStateViewHolder(binding, retry)
        }
    }
}
