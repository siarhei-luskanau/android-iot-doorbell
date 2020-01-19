plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"].toString().toInt())
    buildToolsVersion = rootProject.extra["buildToolsVersion"].toString()

    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"].toString().toInt())
        targetSdkVersion(rootProject.extra["targetSdkVersion"].toString().toInt())
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common:common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")

    //android test, but implementation
    implementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlinVersion"]}")
    implementation("androidx.test.espresso:espresso-core:${rootProject.extra["espressoVersion"]}")
    implementation("androidx.test.espresso:espresso-intents:${rootProject.extra["espressoVersion"]}")
    implementation("androidx.test.espresso:espresso-contrib:${rootProject.extra["espressoVersion"]}")
    implementation("androidx.test:core:${rootProject.extra["androidTestCoreVersion"]}")
    implementation("androidx.test.ext:truth:${rootProject.extra["androidTestCoreVersion"]}")
    implementation("androidx.test.ext:junit-ktx:${rootProject.extra["testExtJunitVersion"]}")
}
