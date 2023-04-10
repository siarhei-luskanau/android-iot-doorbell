plugins {
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinxCoroutinesCore)

    // unit test
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockkCore)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)
}
