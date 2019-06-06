package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun <B : ViewDataBinding> create(
            inflater: LayoutInflater,
            layoutResId: Int,
            parent: ViewGroup
        ): BindingViewHolder<B> {
            val binding = DataBindingUtil.inflate<B>(inflater, layoutResId, parent, false)
            return BindingViewHolder(binding)
        }
    }
}
