import java.io.File
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    val file = File(rootProject.rootDir, "local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

val wbTestApiKey = localProperties.getProperty("WB_TEST_API_KEY") ?: ""
val wbTestApiBaseUrl =
    localProperties.getProperty("WB_TEST_API_BASE_URL") ?: "https://content-api-sandbox.wildberries.ru/"
val wbApiKey = localProperties.getProperty("WB_API_KEY") ?: ""
val wbApiBaseUrl =
    localProperties.getProperty("WB_API_BASE_URL") ?: "https://content-api.wildberries.ru/"
val wbOrdersApiBaseUrl =
    localProperties.getProperty("WB_ORDERS_API_BASE_URL") ?: "https://statistics-api.wildberries.ru/"

val wbOrdersSandboxApiBaseUrl =
    localProperties.getProperty("WB_ORDERS_API_SANDBOX_BASE_URL") ?: "https://statistics-api-sandbox.wildberries.ru/"
android {
    namespace = "com.wbprofit.core.network.impl"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        buildConfigField("String", "WB_TEST_API_KEY", "\"$wbTestApiKey\"")
        buildConfigField("String", "WB_TEST_API_BASE_URL", "\"$wbTestApiBaseUrl\"")
        buildConfigField("String", "WB_API_KEY", "\"$wbApiKey\"")
        buildConfigField("String", "WB_API_BASE_URL", "\"$wbApiBaseUrl\"")
        buildConfigField("String", "WB_ORDERS_API_BASE_URL", "\"$wbOrdersApiBaseUrl\"")
        buildConfigField("String", "WB_ORDERS_API_SANDBOX_BASE_URL", "\"$wbOrdersSandboxApiBaseUrl\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}



dependencies {
    //Net
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)

    //Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.retrofit)
    ksp(libs.moshi.kotlin.codegen)

    //DI
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    //Project
    implementation(project(":core:network:api"))
    implementation(project(":core:keystore:api"))

}

