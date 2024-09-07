plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"

}

android {
    namespace = "com.example.coffeeshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.coffeeshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hiltViewModel()
    implementation(libs.androidx.hilt.navigation.compose)
    //collectAsStateWithLifecycle()
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Room
    implementation (libs.androidx.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    //NavHost()
    implementation(libs.androidx.navigation.compose)
    //bottom navigation
    implementation(libs.androidx.material)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)

    //Firebase Authentication
    implementation(libs.firebase.auth)

    //read data from Firebase
    implementation (libs.google.firebase.database.ktx)


    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    //add icons
    implementation (libs.androidx.material.icons.extended) // Or the latest version


    //splashScreen
    implementation (libs.androidx.core.splashscreen)

    //coil to display images as url
    implementation(libs.coil.compose)

}

// Allow references to generated code
kapt {
    correctErrorTypes =  true
}