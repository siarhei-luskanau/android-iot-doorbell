import java.util.Properties

private object Versions {
    private val versionsProperties = Properties().apply {
        Versions::class.java.classLoader.getResourceAsStream("build_src_versions.properties")
            .use { load(it) }
    }

    // Libraries
    val androidToolsBuildGradle: String =
        versionsProperties["version.androidToolsBuildGradle"].toString()
    val kotlin: String = versionsProperties["version.kotlin"].toString()
    val kotlinxCoroutines: String = versionsProperties["version.kotlinxCoroutines"].toString()
    val karumiShot: String = versionsProperties["version.karumiShot"].toString()
    const val activity = "1.6.1"
    const val androidxCamera = "1.3.0-alpha03"
    const val androidxStartup = "1.1.1"
    const val coil = "2.2.2"
    const val composeBom = "2023.01.00"
    const val composeMaterialAdapter = "1.2.0"
    const val desugar = "2.0.0"
    const val firebaseBom = "31.2.0"
    const val fragment = "1.5.4"
    const val googleServices = "4.3.15"
    const val kotlinxDatetime = "0.4.0"
    const val kotlinxSerialization = "1.4.1"
    const val lifecycle = "2.5.1"
    const val material = "1.8.0"
    const val navigation = "2.5.3"
    const val paging = "3.2.0-alpha03"
    const val pagingCompose = "1.0.0-alpha17"
    const val paperwork = "1.2.7"
    const val room = "2.4.3"
    const val timber = "5.0.1"
    const val workManager = "2.8.0-beta02"

    // DI
    const val dagger = "2.44"
    const val kodein = "7.15.1"
    const val koin = "3.3.0"
    const val toothpick = "3.1.0"

    // Development
    const val leakCanary = "2.10"

    // Testing
    const val mockk = "1.13.3"

    // test instrumentation
    const val androidTestCore = "1.5.0"
    const val androidTestTruth = "1.5.0"
    const val espresso = "3.5.0"
    const val testExtJunit = "1.1.4"
}

object PublicVersions {
    val kotlin = Versions.kotlin
    const val composeCompiler = "1.4.0"
    const val ktlint = "0.48.1"
    const val detekt = "1.22.0"
    const val kotlinxKover = "0.6.1"
}

object BuildVersions {
    const val platformVersion = 33
    const val compileSdkVersion = platformVersion
    const val targetSdkVersion = 33
    const val buildToolsVersion = "33.0.1"
    const val minSdkVersion = 26
}

object Libraries {
    val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val kotlinxDatetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}"
    const val kotlinxSerializationJson =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
    const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val androidxCameraCamerae2 =
        "androidx.camera:camera-camera2:${Versions.androidxCamera}"
    const val androidxCameraLifecycle =
        "androidx.camera:camera-lifecycle:${Versions.androidxCamera}"
    const val androidxCameraView = "androidx.camera:camera-view:${Versions.androidxCamera}"
    const val androidxCameraExtensions =
        "androidx.camera:camera-extensions:${Versions.androidxCamera}"
    const val androidxStartup = "androidx.startup:startup-runtime:${Versions.androidxStartup}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeAnimation = "androidx.compose.animation:animation"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeMaterialTheme =
        "com.google.android.material:compose-theme-adapter:${Versions.composeMaterialAdapter}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activity}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
    const val pagingCommonKtx = "androidx.paging:paging-common-ktx:${Versions.paging}"
    const val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val pagingCompose = "androidx.paging:paging-compose:${Versions.pagingCompose}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilCompose = "io.coil-kt:coil-compose:${Versions.coil}"
    const val paperwork = "hu.supercluster:paperwork:${Versions.paperwork}"

    // firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    const val firebaseDatabaseKtx = "com.google.firebase:firebase-database-ktx"
    const val firebaseStorageKtx = "com.google.firebase:firebase-storage-ktx"

    // hilt
    const val hilt = "com.google.dagger:hilt-android:${Versions.dagger}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.dagger}"

    // dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    // kodein
    const val kodeinDiFrameworkAndroidCore =
        "org.kodein.di:kodein-di-framework-android-core:${Versions.kodein}"

    // koin
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"

    // toothpick
    const val toothpickKtp = "com.github.stephanenicolas.toothpick:ktp:${Versions.toothpick}"
    const val toothpickCompiler =
        "com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothpick}"

    // Development
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
}

object TestLibraries {
    val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"
    const val androidTestCoreKtx = "androidx.test:core-ktx:${Versions.androidTestCore}"
    const val androidTestExtTruth = "androidx.test.ext:truth:${Versions.androidTestTruth}"
    const val composeUiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"
    const val mockkCore = "io.mockk:mockk:${Versions.mockk}"
    const val pagingTesting = "androidx.paging:paging-testing:${Versions.paging}"
    const val testEspressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val testEspressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val testExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.testExtJunit}"
}

object GradlePlugin {
    const val googleServicePlugin = "com.google.gms:google-services:${Versions.googleServices}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.dagger}"
    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val paperworkPlugin = "hu.supercluster:paperwork-plugin:${Versions.paperwork}"
    val androidToolsBuildGradle =
        "com.android.tools.build:gradle:${Versions.androidToolsBuildGradle}"
    val karumiShotPlugin = "com.karumi:shot:${Versions.karumiShot}"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}
