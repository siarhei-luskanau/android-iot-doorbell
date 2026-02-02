plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gson)
    implementation(libs.android.gradle.plugin)
    implementation(libs.jetbrains.compose.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.jetbrains.compose.compiler.plugin)
}

gradlePlugin {
    plugins {
        create("multiplatformDaggerConvention") {
            id = "multiplatformDaggerConvention"
            implementationClass = "MultiplatformDaggerConventionPlugin"
        }
    }
}
