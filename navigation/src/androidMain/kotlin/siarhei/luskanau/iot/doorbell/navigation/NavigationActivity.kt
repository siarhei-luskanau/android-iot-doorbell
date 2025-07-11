package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.serialization.Serializable
import siarhei.luskanau.iot.doorbell.ui.common.theme.AppTheme
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsComposable
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsViewModelMoko
import siarhei.luskanau.iot.doorbell.ui.splash.SplashComposable
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModelImpl

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val permissionsControllerFactory: PermissionsControllerFactory =
                rememberPermissionsControllerFactory()
            val permissionsController: PermissionsController =
                remember(permissionsControllerFactory) {
                    permissionsControllerFactory.createPermissionsController()
                }
            BindEffect(permissionsController)

            AppTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Splash
                ) {
                    composable<AppRoutes.Splash> {
                        SplashComposable(
                            viewModel = viewModel {
                                SplashViewModelImpl(onSplashScreenCompleted = {
                                    navController.navigate(AppRoutes.Permissions) {
                                        launchSingleTop = true
                                        popUpTo<AppRoutes.Splash> { inclusive = true }
                                    }
                                })
                            }
                        )
                    }
                    composable<AppRoutes.Permissions> {
                        PermissionsComposable(
                            viewModel = viewModel {
                                PermissionsViewModelMoko(
                                    onPermissionScreenCompleted = {
                                        navController.navigate(AppRoutes.Auth) {
                                            launchSingleTop = true
                                            popUpTo<AppRoutes.Permissions> { inclusive = true }
                                        }
                                    },
                                    permissionsController = permissionsController
                                )
                            }
                        )
                    }
                    composable<AppRoutes.Auth> {
                        Text(
                            text = "Auth",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    composable<AppRoutes.DoorbellList> {
                        Text(
                            text = "DoorbellList",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

internal sealed interface AppRoutes {
    @Serializable data object Splash : AppRoutes

    @Serializable data object Permissions : AppRoutes

    @Serializable data object Auth : AppRoutes

    @Serializable data object DoorbellList : AppRoutes
}
