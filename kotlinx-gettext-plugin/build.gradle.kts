import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.compiler.embeddable)

    compileOnly(libs.auto.service.annotations)
    ksp(libs.auto.service.ksp)

    testImplementation(libs.kotlin.test)
    testCompileOnly(libs.auto.service.annotations)
    testImplementation(libs.kotlin.compile.testing)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        languageVersion = KotlinVersion.KOTLIN_2_0
        apiVersion = KotlinVersion.KOTLIN_2_0
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
                name = "Kotlinx Gettext"
                description = "Kotlin Compiler plugin to extract strings for i18n in Gettext format"
                url = "https://github.com/kropp/kotlinx-gettext"

                scm {
                    connection = "scm:git:git://github.com/kropp/kotlinx-gettext"
                    developerConnection = "scm:git:git://github.com/kropp/kotlinx-gettext"
                    url = "https://github.com/kropp/kotlinx-gettext/tree/main"
                }

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developer {
                        id = "kropp"
                        name = "Victor Kropp"
                        email = "victor@kropp.name"
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
