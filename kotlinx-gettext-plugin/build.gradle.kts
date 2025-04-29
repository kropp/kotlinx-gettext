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

tasks.withType<KotlinCompile>().all {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
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
