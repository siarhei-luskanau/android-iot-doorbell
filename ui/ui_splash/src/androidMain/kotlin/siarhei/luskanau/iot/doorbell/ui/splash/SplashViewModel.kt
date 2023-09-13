package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val splashNavigation: SplashNavigation,
) : ViewModel(), SplashPresenter {

    override fun onSplashComplete() {
        viewModelScope.launch {
            delay(DELAY_SPLASH)
            splashNavigation.onSplashComplete()
        }
    }

    companion object {
        private const val DELAY_SPLASH = 1_000L
    }
}
