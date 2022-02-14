import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")

    testImplementation(kotlin("test"))
    testCompileOnly("com.google.auto.service:auto-service-annotations:1.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.7")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("kotlinx-gettext") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}