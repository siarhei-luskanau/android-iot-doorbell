package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModelImpl(private val onSplashScreenCompleted: () -> Unit) : SplashViewModel() {

    override fun onSplashComplete() {
        viewModelScope.launch {
            delay(DELAY_SPLASH)
            onSplashScreenCompleted.invoke()
        }
    }

    companion object {
        private const val DELAY_SPLASH = 1_000L
    }
}
