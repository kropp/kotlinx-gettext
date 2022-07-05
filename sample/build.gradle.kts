import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("name.kropp.kotlinx-gettext") version "0.4.0"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kotlinx-gettext"))
    // in your code replace the previous project dependency with the following one:
    // implementation("name.kropp.kotlinx-gettext:kotlinx-gettext-jvm:0.4.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gettext {
    potFile.set(File(projectDir, "src/messages.pot"))
    keywords.set(listOf("tr","trn:1,2"))
}