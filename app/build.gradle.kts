plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.meta.spatial.plugin)
}

android {
    namespace = "com.example.cyberhud"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cyberhud"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        ndkVersion = "27.0.12077973"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.meta.spatial.sdk.base)
    implementation(libs.meta.spatial.sdk.toolkit)
    implementation(libs.meta.spatial.sdk.vr)
}
