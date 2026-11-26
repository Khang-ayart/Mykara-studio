plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mykara.studio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mykara.studio"
        minSdk = 29
        targetSdk = 34
        // Số phiên bản của bản phát hành: sửa hai dòng này mỗi lần ra bản mới.
        val releaseCode = 1
        val releaseName = "0.1.0"
        // CI (bản dựng thử trên GitHub) đặt GITHUB_RUN_NUMBER: số lớn hơn mọi bản phát hành, nên luôn cài đè được.
        val ci = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
        versionCode = if (ci != null) 1000 + ci else releaseCode
        versionName = if (ci != null) "$releaseName-ci.$ci" else releaseName
    }

    // Khóa ký cố định (chỉ dành cho bản thử nghiệm, không phải khóa phát hành): giúp cài đè giữ nguyên bản vẽ đã lưu.
    signingConfigs.getByName("debug") {
        storeFile = file("debug.keystore")
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.webkit:webkit:1.11.0")
}
