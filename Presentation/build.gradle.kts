plugins {
    alias(libs.plugins.android.library)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.moussa79m.presentation"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        compose=true
    }

}

dependencies {

    implementation("io.github.dokar3:pinchzoomgrid:0.0.5")
    // محرك سوني المفتوح المصدر للشبكات المتداخلة
//    implementation("com.github.arasthel:spannedgridlayoutmanager:3.0.2")
    //video tumb
    implementation("io.coil-kt:coil-video:2.6.0")
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.material3)
    //exoPlayer
    val media3_version = "1.2.1" // أو أحدث إصدار متاح
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    implementation("androidx.media3:media3-ui:$media3_version")
    //icons
    implementation("androidx.compose.material:material-icons-extended-android:1.6.0") // تأكد من استخدام إصدار متوافق مع نسخة Compose لديك
    // ربط موديول الدومين (عشان الـ UseCases)
    implementation(project(":Domain"))

    // مكتبات الـ ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // 1. Compose BOM (لضبط توافق الإصدارات أوتوماتيكياً)
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // 2. مكتبات Compose الأساسية
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // 3. Material Design 3 (للألوان والزراير والـ Scaffold)
    implementation("androidx.compose.material3:material3")

    // 4. Activity Compose (عشان نربط الشاشة بالـ Activity الأساسية)
    implementation("androidx.activity:activity-compose:1.8.2")

    // 5. Coil (المكتبة الأقوى لعرض الصور من الـ Uri في Compose)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Debug tooling (عشان تقدر تعمل Preview للشاشات)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")// عشان يقدر يشوف الـ MediaItem والـ Interface
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation(project(":Data")) // عشان يقدر يشوف الـ MediaItem والـ Interface
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}