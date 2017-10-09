package siarhei.luskanau.iot.doorbell.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class BindableViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final T bindings;

    public BindableViewHolder(final T bindings) {
        super(bindings.getRoot());
        this.bindings = bindings;
    }

    public static <B extends ViewDataBinding> BindableViewHolder<B> create(final LayoutInflater inflater,
                                                                           final int layoutResId,
                                                                           final ViewGroup parent) {
        final B binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
        return new BindableViewHolder<>(binding);
    }

    public T getBindings() {
        return bindings;
    }
}
