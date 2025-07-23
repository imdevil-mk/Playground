@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library")
}

dependencies {
    implementation(project(":lib_assistant"))
    implementation(libs.gson)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}