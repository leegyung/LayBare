plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
}


dependencies {
    implementation(libs.paging3.common)
    implementation(libs.hilt.javax)
}