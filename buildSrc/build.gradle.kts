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
    implementation(libs.jetbrains.compose.compiler.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.roborazzi.gradle.plugin)
}
