plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/LICENSE")
        exclude("META-INF/MANIFEST.MF")
        exclude("META-INF/proguard/coroutines.pro")
    }
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(TestLibraries.kotlinxCoroutinesTest)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(TestLibraries.spekDslJvm)
}
