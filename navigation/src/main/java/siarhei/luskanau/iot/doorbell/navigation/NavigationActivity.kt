package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import siarhei.luskanau.iot.doorbell.navigation.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity(R.layout.activity_navigation) {

    private val binding by viewBinding { activity ->
        ActivityNavigationBinding.bind(activity.findViewById(R.id.container))
    }
    private val navController by lazy {
        Navigation.findNavController(this, R.id.navHostFragment)
    }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
