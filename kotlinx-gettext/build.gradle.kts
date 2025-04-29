import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import java.net.URI

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `java-library`
    `maven-publish`
    signing
}

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().download = false
}

kotlin {
    explicitApi()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm {
        java {
        }
    }

    js(IR) {
        nodejs {
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.okio)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(libs.okio.nodefilesystem)
            }
        }
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        groupId = project.group.toString()
        version = project.version.toString()

        artifact(javadocJar.get())

        pom {
            name = "Kotlinx Gettext"
            description = "Kotlin Gettext i18n library"
            url = "https://github.com/kropp/kotlinx-gettext"

            scm {
                connection = "scm:git:git://github.com/kropp/kotlinx-gettext"
                developerConnection = "scm:git:git://github.com/kropp/kotlinx-gettext"
                url = "https://github.com/kropp/kotlinx-gettext/tree/main"
            }

            licenses {
                license {
                    name = "The Apache License, Version 2.0"
                    url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
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

    repositories {
        maven {
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials(PasswordCredentials::class.java)
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(tasks.withType<Sign>())
}
