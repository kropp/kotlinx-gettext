import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    `maven-publish`
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