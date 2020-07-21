private object Versions {
    // Libraries
    const val androidToolsBuildGradle = "4.1.0-beta04"
    const val kotlin = "1.3.72"
    const val kotlinxCoroutines = "1.3.8"
    const val desugar = "1.0.9"
    const val navigation = "2.3.0"
    const val timber = "4.7.1"
    const val androidxCamera = "1.0.0-beta06"
    const val androidxCameraExt = "1.0.0-alpha13"
    const val material = "1.3.0-alpha01"
    const val swiperefreshlayout = "1.1.0"
    const val activity = "1.2.0-alpha06"
    const val fragment = "1.3.0-alpha06"
    const val androidxCore = "1.5.0-alpha01"
    const val lifecycle = "2.3.0-alpha05"
    const val paging = "3.0.0-alpha02"
    const val room = "2.3.0-alpha01"
    const val workManager = "2.4.0-rc01"
    const val constraintLayout = "2.0.0-beta8"
    const val moshi = "1.9.3"
    const val coil = "0.11.0"
    const val viewBindingPropertyDelegate = "1.0.0-beta1"
    const val paperwork = "1.2.7"

    // GMS versions
    const val firebaseDatabase = "19.3.1"
    const val firebaseStorage = "19.1.1"

    // DI
    const val dagger = "2.28.1"
    const val kodein = "7.0.0"
    const val koin = "2.1.6"
    const val toothpick = "3.1.0"

    // Development
    const val leakCanary = "2.4"

    // Android Things
    const val androidthings = "1.0"

    // Testing
    const val mockitoKotlin = "2.2.0"
    const val spek = "2.0.11"

    // test instrumentation
    const val androidTestCore = "1.3.0-rc01"
    const val espresso = "3.3.0-rc01"
    const val testExtJunit = "1.1.2-rc01"
}

object BuildVersions {
    const val platformVersion = 30
    const val compileSdkVersion = platformVersion
    const val targetSdkVersion = 30
    const val buildToolsVersion = "30.0.0"
    const val minSdkVersion = 21
}

object Libraries {
    const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    const val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val androidxCamera = "androidx.camera:camera-camera2:${Versions.androidxCamera}"
    const val androidxCameraView =
        "androidx.camera:camera-view:${Versions.androidxCameraExt}"
    const val androidxCameraExtensions =
        "androidx.camera:camera-extensions:${Versions.androidxCameraExt}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val lifecycleCommonJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleLivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleViewmodelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val pagingCommonKtx = "androidx.paging:paging-common-ktx:${Versions.paging}"
    const val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val viewBindingPropertyDelegate =
        "com.github.kirich1409:ViewBindingPropertyDelegate:${Versions.viewBindingPropertyDelegate}"
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
    const val androidTestCoreKtx = "androidx.test:core-ktx:${Versions.androidTestCore}"
    const val androidTestExtTruth = "androidx.test.ext:truth:${Versions.androidTestCore}"
    const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val testExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.testExtJunit}"
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