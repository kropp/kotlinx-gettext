import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    wrapper
}

allprojects {
    group = "name.kropp.kotlinx-gettext"
    version = "0.6.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().all {
        sourceCompatibility = "11"
        targetCompatibility = "11"

        options.encoding = "UTF-8"
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.5"
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    distributionSha256Sum = "9d926787066a081739e8200858338b4a69e837c3a821a33aca9db09dd4a41026"
}
