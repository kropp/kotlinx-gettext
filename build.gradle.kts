import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    wrapper
}

allprojects {
    group = "name.kropp.kotlinx-gettext"
    version = rootProject.libs.versions.kotlinxGettext.get()

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().all {
        sourceCompatibility = "11"
        targetCompatibility = "11"

        options.encoding = "UTF-8"
    }

    tasks.withType<KotlinCompile>().all {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
        }
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.14"
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    distributionSha256Sum = "61ad310d3c7d3e5da131b76bbf22b5a4c0786e9d892dae8c1658d4b484de3caa"
}
