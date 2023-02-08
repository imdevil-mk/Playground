plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.imdevil.playgroud"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        isEnabled = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("org.jsoup:jsoup:1.15.2")

    implementation("androidx.fragment:fragment-ktx:1.5.2")

    implementation("com.jakewharton:disklrucache:2.0.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0-alpha02")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0-alpha02")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.0-alpha02")
    // Annotation processor
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.0-alpha02")


    implementation("androidx.media:media:1.6.0")

    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
}