import org.gradle.accessors.dm.LibrariesForLibs
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("maven-publish")
    alias(libs.plugins.compose)
    kotlin("multiplatform")
    id("com.android.library")
}

android {
    namespace = "com.netguru.multiplatform.charts"

    compileSdk = 34
    buildFeatures.compose = true

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        /*
        versionCode = 1
        versionName = "0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"*/
    }

    composeOptions.kotlinCompilerExtensionVersion = "1.5.10"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { compose = true }
}

val jvmTarget = "17"
kotlin {

    jvmToolchain(17)
    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = jvmTarget
        }
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.uiUtil)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.runtimeSaveable)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

val mavenPropertiesFile = rootProject.file("publishing.properties")
val mavenProperties = Properties().apply {
    load(FileInputStream(mavenPropertiesFile))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kalist28/compose-multiplatform-charts")
            credentials {
                username = mavenProperties["user"] as String
                password = mavenProperties["token"] as String
            }
        }
    }
}

group = "com.netguru.multiplatform"
version = "0.0.1"
