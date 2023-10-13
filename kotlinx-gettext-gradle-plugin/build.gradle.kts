import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("com.gradle.plugin-publish") version "1.2.1"
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
            website = "https://github.com/kropp/kotlinx-gettext"
            vcsUrl = "https://github.com/kropp/kotlinx-gettext"
            tags = listOf("kotlin", "i18n", "gettext")

            implementationClass = "name.kropp.kotlinx.gettext.gradle.KotlinxGettextGradlePlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

signing {
    useGpgCmd()
}
