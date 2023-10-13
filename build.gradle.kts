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
    gradleVersion = "8.4"
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    distributionSha256Sum = "3e1af3ae886920c3ac87f7a91f816c0c7c436f276a6eefdb3da152100fef72ae"
}
