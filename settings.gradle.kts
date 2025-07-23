enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Playground"
include(":app")
include(":lib_kapt")
include(":lib_ksp")
include(":lib_sdk")
include(":lib_assistant")
include(":lib_assistant_compiler")
