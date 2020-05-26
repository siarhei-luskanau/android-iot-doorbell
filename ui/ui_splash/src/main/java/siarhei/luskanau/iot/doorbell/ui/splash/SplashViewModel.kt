package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import siarhei.luskanau.iot.doorbell.common.AppNavigation

class SplashViewModel(
    private val appNavigation: AppNavigation
) : ViewModel(), SplashPresenter {

    override fun onResume() {
        viewModelScope.launch {
            delay(DELAY_SPLASH)
            appNavigation.goSplashToDoorbellList()
        }
    }

    companion object {
        private const val DELAY_SPLASH = 1_000L
    }
}
