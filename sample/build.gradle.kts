import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("name.kropp.kotlinx-gettext") version "0.2"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("name.kropp.kotlinx-gettext:kotlinx-gettext:0.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gettext {
    potFile.set(File(projectDir, "src/messages.pot"))
    keywords.set(listOf("tr","trn:1,2"))
}