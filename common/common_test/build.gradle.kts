plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    packagingOptions.setExcludes(
        setOf(
            "META-INF/AL2.0",
            "META-INF/LGPL2.1",
            "META-INF/LICENSE",
            "META-INF/MANIFEST.MF",
            "META-INF/proguard/coroutines.pro"
        )
    )
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(TestLibraries.kotlinxCoroutinesTest)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(TestLibraries.spekDslJvm)
}
