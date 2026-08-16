import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.net.URI

// NOTE: java-library is intentionally NOT applied here. Kotlin 2.3's
// checkKotlinGradlePluginConfigurationErrors reports java/java-library as
// incompatible with the Kotlin multiplatform plugin.
plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
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
        val okioVersion = "3.2.0"
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.squareup.okio:okio:$okioVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("com.squareup.okio:okio-nodefilesystem:$okioVersion")
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
            name.set("Kotlinx Gettext")
            description.set("Kotlin Gettext i18n library")
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