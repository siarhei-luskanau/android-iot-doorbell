plugins {
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(libs.kotlinx.coroutines.core)

    // unit test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
