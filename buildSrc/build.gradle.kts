plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.gson)
    implementation(libs.roborazzi.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
}
