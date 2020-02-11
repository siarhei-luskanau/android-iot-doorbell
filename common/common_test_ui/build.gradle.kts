plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)

    // android test, but implementation
    implementation(TestLibraries.kotlinTest)
    implementation(TestLibraries.testEspressoCore)
    implementation(TestLibraries.testEspressoIntents)
    implementation(TestLibraries.testEspressoContrib)
    implementation(TestLibraries.androidTestCore)
    implementation(TestLibraries.androidTestExtTruth)
    implementation(TestLibraries.testExtJunit)
}
