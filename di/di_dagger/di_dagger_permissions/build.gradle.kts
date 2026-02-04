plugins {
    id("multiplatformConvention")
    id("com.google.devtools.ksp")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.dagger.permissions"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":di:di_dagger:di_dagger_common"))
            implementation(libs.dagger)
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
