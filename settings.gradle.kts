pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kotlinx-gettext"

include("kotlinx-gettext", "kotlinx-gettext-plugin", "kotlinx-gettext-gradle-plugin")
