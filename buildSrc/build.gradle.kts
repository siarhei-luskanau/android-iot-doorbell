import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

val versions = loadProperties("$projectDir/src/main/resources/build_src_versions.properties")

dependencies{
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions["version.kotlin"]}")
    implementation("com.android.tools.build:gradle:${versions["version.androidToolsBuildGradle"]}")
    implementation("com.karumi:shot:${versions["version.karumiShot"]}")
}