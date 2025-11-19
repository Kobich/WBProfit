import java.io.File
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    val file = File(rootProject.rootDir, "local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

val wbTestApiKey = localProperties.getProperty("WB_TEST_API_KEY") ?: ""
val wbTestApiBaseUrl = localProperties.getProperty("WB_TEST_API_BASE_URL") ?: "https://content-api-sandbox.wildberries.ru/"
val wbApiKey = localProperties.getProperty("WB_API_KEY") ?: ""
val wbApiBaseUrl = localProperties.getProperty("WB_API_BASE_URL") ?: "https://content-api.wildberries.ru/"

android {
    namespace = "com.wbprofit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wbprofit"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "WB_API_KEY", "\"$wbApiKey\"")
        buildConfigField("String", "WB_API_BASE_URL", "\"$wbApiBaseUrl\"")
        buildConfigField("String", "WB_TEST_API_KEY", "\"$wbTestApiKey\"")
        buildConfigField("String", "WB_TEST_API_BASE_URL", "\"$wbTestApiBaseUrl\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    //Project modules
    implementation(project(":core:network:impl"))

    implementation(project(":core:keystore:api"))
    implementation(project(":core:keystore:impl"))

    implementation(project(":feature:analytics:api"))
    implementation(project(":feature:analytics:impl"))
    implementation(project(":feature:cards:impl"))
    implementation(project(":feature:cards:api"))
    implementation(project(":feature:auth:impl"))
    implementation(project(":feature:auth:api"))

    implementation(project(":ui:main:impl"))
    implementation(project(":ui:card:impl"))
    implementation(project(":ui:cards:impl"))
    implementation(project(":ui:main:api"))
    implementation(project(":ui:card:api"))
    implementation(project(":ui:cards:api"))
    implementation(project(":ui:analytics:impl"))
    implementation(project(":ui:analytics:api"))

    implementation(project(":ui:auth:impl"))
    implementation(project(":ui:auth:api"))


    implementation(project(":base:ui"))


    //Logging
    implementation(libs.timber)

    //DI
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    //Net
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.retrofit)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
