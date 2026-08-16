import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(kotlin("stdlib"))
    // Pin kotlin-compiler-embeddable to the same version as the Kotlin Gradle plugin (2.3.10):
    // the compiler plugin must be compiled against the exact compiler version it runs inside.
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.3.10")

    compileOnly("com.google.auto.service:auto-service-annotations:1.0.1")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.2.0")

    testImplementation(kotlin("test"))
    testCompileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    // kotlin-compile-testing 1.5.0 embeds compiler 1.8.0, which cannot read Kotlin 2.3 metadata;
    // the maintained fork kctfork 0.12.1 embeds compiler 2.3.0 (closest to our 2.3.10).
    testImplementation("dev.zacsweers.kctfork:core:0.12.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().all {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        languageVersion.set(KotlinVersion.KOTLIN_2_3)
        apiVersion.set(KotlinVersion.KOTLIN_2_3)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("kotlinx-gettext") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])

            pom {
                name.set("Kotlinx Gettext")
                description.set("Kotlin Compiler plugin to extract strings for i18n in Gettext format")
                url.set("https://github.com/kropp/kotlinx-gettext")

                scm {
                    connection.set("scm:git:git://github.com/kropp/kotlinx-gettext")
                    developerConnection.set("scm:git:git://github.com/kropp/kotlinx-gettext")
                    url.set("https://github.com/kropp/kotlinx-gettext/tree/main")
                }

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("kropp")
                        name.set("Victor Kropp")
                        email.set("victor@kropp.name")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials(PasswordCredentials::class.java)
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["kotlinx-gettext"])
}
