import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.kapt)
}

val localProperties = Properties().apply {
    file("../local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

// Get API keys from environment variables or fall back to local.properties
val apiKey: String =
    System.getenv("FLICKR_API_KEY") ?: localProperties.getProperty("flickr_api_key", "")
val apiSecret: String =
    System.getenv("FLICKR_API_SECRET") ?: localProperties.getProperty("flickr_api_secret", "")


android {
    namespace = "com.vsebastianvc.flickr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vsebastianvc.flickr"
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
            buildConfigField("String", "FLICKR_API_KEY", "\"$apiKey\"")
            buildConfigField("String", "FLICKR_API_SECRET", "\"$apiSecret\"")
        }
        debug {
            buildConfigField("String", "FLICKR_API_KEY", "\"$apiKey\"")
            buildConfigField("String", "FLICKR_API_SECRET", "\"$apiSecret\"")
        }
    }

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Flickr
    implementation(libs.flickrj)
    implementation(files("libs/slf4j-android-1.6.1-RC1.jar"))

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.navigation.testing)
    testImplementation(libs.coroutines.test)

    // Coil
    implementation(libs.coil.compose)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.mockito.core)
    testImplementation(libs.koin.test.junit4)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.kotlin)
}