plugins {
    multiplatformConvention
    id("com.google.devtools.ksp")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.permissions"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {

                implementation(project(":di:di_dagger:di_dagger_common"))
                implementation(libs.dagger)
//                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
