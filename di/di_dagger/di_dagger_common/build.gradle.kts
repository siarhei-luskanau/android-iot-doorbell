plugins {
    multiplatformConvention
    kotlin("kapt")
}

android.namespace = "siarhei.luskanau.iot.doorbell.dagger.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":base_android"))
                implementation(project(":base_camera"))
                implementation(project(":base_file"))
                implementation(project(":base_work_manager"))
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":data:dataDoorbellApiFirebase"))
                implementation(project(":data:dataDoorbellApiStub"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.androidx.startup.runtime)
                implementation(libs.androidx.work.runtime.ktx)
                implementation(libs.dagger)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}

dependencies {
    kapt(libs.dagger.compiler)
}
