plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("com.google.devtools.ksp") version "1.6.10-1.0.2" apply false
}

allprojects {
    group = "com.github.kropp.kotlinx-gettext"
    version = "0.1.1"

    repositories {
        mavenCentral()
    }
}