plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(buildSrcLibs.android.gradle.plugin)
    implementation(buildSrcLibs.gson)
    implementation(buildSrcLibs.roborazzi.gradle.plugin)
    implementation(buildSrcLibs.kotlin.gradle.plugin)
}
