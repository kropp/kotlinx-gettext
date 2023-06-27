import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("com.gradle.plugin-publish") version "1.0.0"
    signing
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))
    testImplementation(kotlin("test"))
}

gradlePlugin {
    plugins {
        create("kotlinx-gettext") {
            id = "name.kropp.kotlinx-gettext"
            displayName = "Kotlinx Gettext"
            description = "Extract strings for i18n from Kotlin files in Gettext format"
            implementationClass = "name.kropp.kotlinx.gettext.gradle.KotlinxGettextGradlePlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

signing {
    useGpgCmd()
}
