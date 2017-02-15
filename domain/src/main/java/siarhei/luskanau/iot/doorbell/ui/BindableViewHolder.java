package siarhei.luskanau.iot.doorbell.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class BindableViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private T bindings;

    public BindableViewHolder(T bindings) {
        super(bindings.getRoot());
        this.bindings = bindings;
    }

    public static <B extends ViewDataBinding> BindableViewHolder<B> create(LayoutInflater inflater,
                                                                           int layoutResId, ViewGroup parent) {
        B binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
        return new BindableViewHolder<>(binding);
    }

    public T getBindings() {
        return bindings;
    }
}
