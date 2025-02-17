plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.lecturfy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lecturfy"
        minSdk = 26
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room components
    implementation ("androidx.room:room-ktx:2.2.5")
    implementation ("androidx.room:room-runtime:2.2.5")
    implementation ("androidx.constraintlayout:constraintlayout:1.1.3")
    kapt ("androidx.room:room-compiler:2.2.5")
    androidTestImplementation ("androidx.room:room-testing:2.2.5")
    
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.jayway.jsonpath:json-path:2.6.0")
    implementation ("com.google.android.gms:play-services-auth:20.0.0")
implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.media3:media3-common:1.3.1")
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
}
