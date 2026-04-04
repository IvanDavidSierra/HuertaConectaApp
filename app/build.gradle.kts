plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "co.ue.edu.huertaconectaapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "co.ue.edu.huertaconectaapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.recyclerview.selection)
    implementation(libs.cardview)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.loggin)
    implementation(libs.gson)
    implementation(libs.shimmer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}