package siarhei.luskanau.iot.doorbell.ui.base.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

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
