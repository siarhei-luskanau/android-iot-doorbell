import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

val versions = loadProperties("$projectDir/src/main/resources/build_src_versions.properties")

dependencies {
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions["version.kotlinxCoroutines"]}")
}
