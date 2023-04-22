plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies{
    implementation(buildSrcLibs.karumiShot)
    implementation(buildSrcLibs.kotlin.gradle.plugin)
    implementation(buildSrcLibs.android.gradle.plugin)
}