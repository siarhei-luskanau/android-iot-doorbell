package siarhei.luskanau.iot.doorbell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasAndroidxFragmentInjector
import javax.inject.Inject

class AppActivity : AppCompatActivity(), HasAndroidxFragmentInjector {

    @Inject
    lateinit var androidxFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        navigationController.navigateToDoorbells()
    }

    override fun androidxFragmentInjector() = androidxFragmentInjector

}
