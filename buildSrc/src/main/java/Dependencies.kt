private object Versions {
    // Libraries
    const val androidToolsBuildGradle = "4.1.0-alpha08"
    const val kotlin = "1.3.72"
    const val kotlinxCoroutines = "1.3.5"
    const val navigation = "2.3.0-alpha06"
    const val timber = "4.7.1"
    const val androidxCamera = "1.0.0-beta03"
    const val androidxCameraExtensions = "1.0.0-alpha10"
    const val material = "1.2.0-alpha06"
    const val swiperefreshlayout = "1.1.0-rc01"
    const val fragment = "1.3.0-alpha04"
    const val androidxCore = "1.3.0-rc01"
    const val lifecycle = "2.3.0-alpha02"
    const val paging = "2.1.2"
    const val room = "2.2.5"
    const val workManager = "2.4.0-alpha03"
    const val constraintLayout = "2.0.0-beta4"
    const val moshi = "1.9.2"
    const val coil = "0.10.1"
    const val paperwork = "1.2.7"

    // GMS versions
    const val firebaseDatabase = "19.3.0"
    const val firebaseStorage = "19.1.1"

    // DI
    const val dagger = "2.27"
    const val kodein = "6.5.5"
    const val koin = "2.1.5"
    const val toothpick = "3.1.0"

    // Development
    const val leakCanary = "2.2"

    // Android Things
    const val androidthings = "1.0"

    // Testing
    const val mockitoKotlin = "2.2.0"
    const val spek = "2.0.10"

    // test instrumentation
    const val androidTestCore = "1.3.0-beta01"
    const val espresso = "3.3.0-beta01"
    const val testExtJunit = "1.1.2-beta01"
}

object BuildVersions {
    const val platformVersion = "R"
    const val compileSdkVersion = "android-$platformVersion"
    const val targetSdkVersion = 30
    const val buildToolsVersion = "30.0.0-rc3"
    const val minSdkVersion = 21
}

object Libraries {
    const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:1.0.5"
    const val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val androidxCamera = "androidx.camera:camera-camera2:${Versions.androidxCamera}"
    const val androidxCameraView = "androidx.camera:camera-view:${Versions.androidxCameraExtensions}"
    const val androidxCameraExtensions =
        "androidx.camera:camera-extensions:${Versions.androidxCameraExtensions}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val lifecycleCommonJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleLivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleViewmodelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val pagingCommonKtx = "androidx.paging:paging-common-ktx:${Versions.paging}"
    const val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val paperwork = "hu.supercluster:paperwork:${Versions.paperwork}"

    // firebase
    const val firebaseDatabaseKtx =
        "com.google.firebase:firebase-database-ktx:${Versions.firebaseDatabase}"
    const val firebaseStorageKtx =
        "com.google.firebase:firebase-storage-ktx:${Versions.firebaseStorage}"

    // dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    // kodein
    const val kodeinDiGenericJvm = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodein}"
    const val kodeinDiFrameworkAndroidX =
        "org.kodein.di:kodein-di-framework-android-x:${Versions.kodein}"

    // koin
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"

    // toothpick
    const val toothpickKtp = "com.github.stephanenicolas.toothpick:ktp:${Versions.toothpick}"
    const val toothpickCompiler =
        "com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothpick}"

    // Development
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    // Android Things
    const val androidthings = "com.google.android.things:androidthings:${Versions.androidthings}"
}

object TestLibraries {

    const val spekRunnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:${Versions.spek}"
    const val spekDslJvm = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}"
    const val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    const val androidTestCore = "androidx.test:core:${Versions.androidTestCore}"
    const val androidTestExtTruth = "androidx.test.ext:truth:${Versions.androidTestCore}"
    const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val testExtJunit = "androidx.test.ext:junit-ktx:${Versions.testExtJunit}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val databindingCompiler =
        "androidx.databinding:databinding-compiler:${Versions.androidToolsBuildGradle}"
}

object GradlePlugin {
    const val androidToolsBuildGradle =
        "com.android.tools.build:gradle:${Versions.androidToolsBuildGradle}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val androidJunit5Plugin = "de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0"
    const val googleServicePlugin = "com.google.gms:google-services:4.3.3"
    const val paperworkPlugin = "hu.supercluster:paperwork-plugin:${Versions.paperwork}"
    const val koinGradlePlugin = "org.koin:koin-gradle-plugin:${Versions.koin}"
}