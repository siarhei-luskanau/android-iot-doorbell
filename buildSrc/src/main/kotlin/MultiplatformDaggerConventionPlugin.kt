import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.AbstractTestTask
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformDaggerConventionPlugin : Plugin<Project> {
    @OptIn(ExperimentalComposeLibrary::class)
    override fun apply(project: Project) {
        val versionCatalogs = project.extensions.getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.plugins.apply("com.android.library")
        project.plugins.apply("org.jetbrains.compose")
        project.plugins.apply("org.jetbrains.kotlin.plugin.compose")

        val kotlin = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.jvmToolchain(21)

        kotlin.androidTarget {
            compilerOptions {
                jvmTarget.set(
                    JvmTarget.fromTarget(
                        versionCatalogs.findVersion("build-jvmTarget").get().requiredVersion
                    )
                )
                freeCompilerArgs.add(
                    "-Xjdk-release=${
                        JavaVersion.valueOf(
                            versionCatalogs.findVersion("build-javaVersion").get().requiredVersion
                        )
                    }"
                )
            }
        }

        val compose = project.extensions.getByType(ComposeExtension::class.java).dependencies

        kotlin.sourceSets.getByName("commonMain") {}
        kotlin.sourceSets.getByName("commonTest") {}
        kotlin.sourceSets.getByName("androidMain").dependencies {
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(versionCatalogs.findLibrary("android-material").get())
            implementation(versionCatalogs.findLibrary("androidx-activity-compose").get())
            implementation(versionCatalogs.findLibrary("androidx-activity-ktx").get())
            implementation(versionCatalogs.findLibrary("androidx-tracing").get())
            implementation(versionCatalogs.findLibrary("coil-compose").get())
            implementation(compose.material)
            implementation(compose.uiTooling)
            implementation(versionCatalogs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
            implementation(versionCatalogs.findLibrary("jetbrains-navigation-compose").get())
        }
        kotlin.sourceSets.getByName("androidUnitTest").dependencies {
            implementation(versionCatalogs.findLibrary("androidx-paging-testing").get())
            implementation(versionCatalogs.findLibrary("espresso-core").get())
            implementation(versionCatalogs.findLibrary("kotlin-test").get())
            implementation(versionCatalogs.findLibrary("kotlinx-coroutines-test").get())
            implementation(versionCatalogs.findLibrary("mockk").get())
        }
        kotlin.sourceSets.getByName("androidInstrumentedTest").dependencies {
            implementation(versionCatalogs.findLibrary("espresso-core").get())
            implementation(versionCatalogs.findLibrary("kotlin-test").get())
            implementation(compose.uiTest)
        }

        val android = project.extensions.getByType(LibraryExtension::class.java)
        android.compileSdk = versionCatalogs.findVersion("android-build-compileSdk").get().requiredVersion.toInt()
        android.defaultConfig.minSdk =
            versionCatalogs.findVersion("android-build-minSdk").get().requiredVersion.toInt()
        android.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        android.buildFeatures.buildConfig = false
        android.compileOptions.sourceCompatibility = JavaVersion.valueOf(
            versionCatalogs.findVersion("build-javaVersion").get().requiredVersion
        )
        android.compileOptions.targetCompatibility = JavaVersion.valueOf(
            versionCatalogs.findVersion("build-javaVersion").get().requiredVersion
        )
        android.testOptions.configureAndroidTestOptions()
        android.packaging.resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/LICENSE-notice.md")
            add("META-INF/LICENSE.md")
            add("META-INF/com.google.dagger_dagger.version")
        }

        project.tasks.withType(AbstractTestTask::class.java).configureEach(
            org.gradle.api.Action<AbstractTestTask> { failOnNoDiscoveredTests.set(false) }
        )
    }
}
