import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.20" apply false
    id("com.google.devtools.ksp") version "1.7.20-1.0.6" apply false
    wrapper
}

allprojects {
    group = "name.kropp.kotlinx-gettext"
    version = "0.5.0"

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
    gradleVersion = "7.5"
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    distributionSha256Sum = "cb87f222c5585bd46838ad4db78463a5c5f3d336e5e2b98dc7c0c586527351c2"
}
