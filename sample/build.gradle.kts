plugins {
    kotlin("jvm")
    id("name.kropp.kotlinx-gettext") version "0.5.2"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("name.kropp.kotlinx-gettext:kotlinx-gettext-jvm:0.5.2")
}

gettext {
    potFile.set(File(projectDir, "src/messages.pot"))
    keywords.set(listOf("tr","trn:1,2"))
}