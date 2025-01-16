plugins {
    id("kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    libs.bundles.boms
        .get()
        .forEach { implementation(platform(it)) }
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.kotlinxSerialization)
    compileOnly(libs.bundles.kotlinLogging)
    testImplementation(libs.bundles.kotlinTest)
}
