package siarhei.luskanau.iot.doorbell.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.R
import siarhei.luskanau.iot.doorbell.di.common.Injectable

abstract class BaseAppFragment<B : ViewDataBinding> : Fragment(), Injectable {

    protected lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<B>(inflater, getViewLayout(), container, false)
                    .also {
                        binding = it
                        binding.setLifecycleOwner(this)
                    }
                    .root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.title = getActionBarTitle()
    }

    protected open fun getActionBarTitle(): String = getString(R.string.app_name)

    @LayoutRes
    protected abstract fun getViewLayout(): Int
}