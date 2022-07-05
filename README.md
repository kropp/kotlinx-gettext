# Kotlin Gettext

Gettext is a widely used internationalization (i18n) format supported by many tools.
This project provides pure Kotlin implementation of Gettext.

The project consists of several parts:
 1. `kotlinx-gettext` library to use in your project to translate strings.
 2. `kotlinx-gettext-plugin` Kotlin compiler plugin to extract strings for translation from Kotlin code.
 3. `kotlinx-gettext-gradle-plugin` to conveniently apply Kotlin compiler plugin in your Gradle build.

## Usage

### Library

Add the `"name.kropp.kotlinx-gettext:kotlinx-gettext:0.4.0"` to the dependencies of your project.
The library is available on [Maven Central](https://search.maven.org/artifact/name.kropp.kotlinx-gettext/kotlinx-gettext).

Load translated strings and apply translations using an instance of `I18n` class.

```kotlin
val i18n = Gettext.load(Locale.GERMAN, Thread.currentThread().contextClassLoader.getResourceAsStream("de.po")!!)
println(i18n.tr("Hello world!"))
```

See `sample` module for a full example. Refer to [GNU gettext documentation](https://www.gnu.org/software/gettext/)
for more details.

### Translation

Apply Gradle plugin to extract strings and setup `gettext` task: 

```kotlin
plugins {
  id("name.kropp.kotlinx-gettext") version "0.4.0"
}

gettext {
  potFile.set(File(projectDir, "src/messages.pot"))
  keywords.set(listOf("tr","trn:1,2"))
}
```

Then invoke `./gradlew gettext` to extract strings for translations into `src/messages.pot`

Translate messages with any software or service supporting Gettext format. The result will be a number of .po files,
which you should put into resources.

## Known issues
 * The library only supports Kotlin/JVM and Kotlin/JS as of now, Multiplatform Native port is in progress.
 * .mo files are not (yet) supported.