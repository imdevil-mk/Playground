@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("imdevil.build.application")
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.kapt")
}

android {

    defaultConfig {
        applicationId = "com.imdevil.playgroud"
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

    /* ksp生成的类使用AS无法访问到
    applicationVariants.all {
        val variantName = name
        sourceSets {
            getByName("main") {
                java.srcDir(File("build/generated/ksp/$variantName/kotlin"))
            }
        }
    }
    */
}

dependencies {
    implementation(project(":lib_sdk"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //for views & widgets
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.preference)

    // network
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)

    // moshi
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.glide.core)

    implementation(libs.androidx.media)

    implementation(libs.jsoup)
    implementation(libs.disklrucache)
    implementation(libs.rxjava3)
    implementation(libs.rxandroid)

    implementation(project(":lib_kapt"))
    kapt(project(":lib_kapt"))

    implementation(project(":lib_assistant"))
    kapt(project(":lib_assistant_compiler"))

    implementation(project(":lib_ksp"))
    ksp(project(":lib_ksp"))

    //for test
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}