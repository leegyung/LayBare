import org.jetbrains.kotlin.ir.backend.js.compile
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.project.laybare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.laybare"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        debug {
            buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
            buildConfigField("String", "SEARCH_ENGINE", "\"${properties.getProperty("SEARCH_ENGINE")}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        release {
            buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
            buildConfigField("String", "SEARCH_ENGINE", "\"${properties.getProperty("SEARCH_ENGINE")}\"")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.fragment)
    implementation(libs.navigation)
    implementation(libs.glide)
    implementation(libs.recyclerView)
    implementation(libs.photoView)
    implementation(libs.gson)

    implementation(libs.imageRecognition)
    implementation(libs.google.map)

}