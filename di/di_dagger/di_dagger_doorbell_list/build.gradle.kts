plugins {
    id("multiplatformConvention")
    id("com.google.devtools.ksp")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.dagger.doorbelllist"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":di:di_dagger:di_dagger_common"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_doorbell_list"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.dagger)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

dependencies {
    kspAndroid(libs.dagger.compiler)
}
