plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    packagingOptions {
        excludes = setOf(
            "META-INF/AL2.0",
            "META-INF/LGPL2.1",
            "META-INF/LICENSE",
            "META-INF/MANIFEST.MF",
            "META-INF/proguard/coroutines.pro"
        )
    }
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(TestLibraries.kotlinxCoroutinesTest)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(TestLibraries.spekDslJvm)
}
