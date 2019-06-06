package siarhei.luskanau.iot.doorbell.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

abstract class BaseRecyclerBindingPagingAdapter<T, B : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : BaseRecyclerClickablePagingAdapter<T, BindingViewHolder<B>>(
        diffCallback
) {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<B> =
            BindingViewHolder.create(LayoutInflater.from(parent.context), getViewLayout(), parent)

    @LayoutRes
    protected abstract fun getViewLayout(): Int
}
