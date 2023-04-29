plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.koin.android)
                implementation(libs.timber)
            }
        }
    }
}
