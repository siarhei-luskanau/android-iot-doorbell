package siarhei.luskanau.iot.doorbell.ui.base.adapter

import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseRecyclerBindingAdapter<T, B : ViewDataBinding> : BaseRecyclerArrayAdapter<T, BindingViewHolder<B>>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<B> =
            BindingViewHolder.create<B>(LayoutInflater.from(parent.context), getViewLayout(), parent)

    @LayoutRes
    protected abstract fun getViewLayout(): Int

}
