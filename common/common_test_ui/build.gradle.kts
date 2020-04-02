plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)

    // android test, but implementation
    implementation(TestLibraries.kotlinTest)
    implementation(TestLibraries.testEspressoCore)
    implementation(TestLibraries.testEspressoIntents)
    implementation(TestLibraries.testEspressoContrib)
    implementation(TestLibraries.androidTestCore)
    implementation(TestLibraries.androidTestExtTruth)
    implementation(TestLibraries.testExtJunit)
}