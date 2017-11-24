package siarhei.luskanau.iot.doorbell.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public abstract class BindingFrameLayout<B extends ViewDataBinding> extends FrameLayout {

    protected final B bindings;

    {
        bindings = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                getViewLayout(), this, true);
    }

    public BindingFrameLayout(final Context context) {
        super(context);
        init(context, null);
    }

    public BindingFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BindingFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BindingFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(final Context context, final AttributeSet attrs) {
        applyAttributes(context, attrs);

        onViewCreated(context);
    }

    protected void applyAttributes(@SuppressWarnings("unused") final Context context, @SuppressWarnings("unused") final AttributeSet attrs) {
    }

    protected void onViewCreated(@SuppressWarnings("unused") final Context context) {
    }

    @LayoutRes
    protected abstract int getViewLayout();

    @SuppressWarnings("unused")
    public B getBindings() {
        return bindings;
    }
}
