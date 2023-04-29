plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(buildSrcLibs.android.gradle.plugin)
    implementation(buildSrcLibs.karumiShot)
    implementation(buildSrcLibs.kotlin.gradle.plugin)
}
