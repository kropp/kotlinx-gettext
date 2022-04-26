import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("com.gradle.plugin-publish") version "1.0.0-rc-1"
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

pluginBundle {
    website = "https://github.com/kropp/kotlinx-gettext"
    vcsUrl = "https://github.com/kropp/kotlinx-gettext"
    description = "Extract strings for i18n from Kotlin files in Gettext format"
    tags = listOf("kotlin", "i18n", "gettext")
}