package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import siarhei.luskanau.iot.doorbell.navigation.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity(R.layout.activity_navigation) {

    private val binding by lazy {
        ActivityNavigationBinding.bind(findViewById(R.id.container))
    }

    private val navController: NavController
        get() = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController

    private val appBarConfiguration: AppBarConfiguration
        get() = AppBarConfiguration(navController.graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
