plugins {
//    alias(libs.plugins.android.library)
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}


dependencies {

    implementation(libs.jcip.annotations)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("junit:junit:4.13.2") // دي مكتبة جافا عادية للاختبار ومسموح بيها

}