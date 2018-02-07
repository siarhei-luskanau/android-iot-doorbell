package siarhei.luskanau.iot.doorbell.ui.base

import android.annotation.TargetApi
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

abstract class BindingFrameLayout<B : ViewDataBinding> : FrameLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    val bindings: B

    @LayoutRes
    protected abstract fun getViewLayout(): Int

    init {
        bindings = DataBindingUtil.inflate(LayoutInflater.from(context),
                this.getViewLayout(), this, true)
    }

    protected fun init(context: Context, attrs: AttributeSet?) {
        applyAttributes(context, attrs)

        onViewCreated(context)
    }

    protected fun applyAttributes(context: Context, attrs: AttributeSet?) {}

    protected fun onViewCreated(context: Context) {}
}
