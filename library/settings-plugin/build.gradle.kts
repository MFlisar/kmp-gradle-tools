import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
}

// -------------------
// Informations
// -------------------

val description = "a gradle settings file plugin that provides common functions for kmp libraries"

// Module
val artifactId = "settings-gradle-plugin"

// Library
val libraryName = "kmp-gradle-tools"
val libraryDescription = "$libraryName - $artifactId module - $description"
val groupID = "io.github.mflisar.kmp-gradle-tools"
val release = 2025
val github = "https://github.com/MFlisar/kmp-gradle-tools"
val license = "Apache License 2.0"
val licenseUrl = "$github/blob/main/LICENSE"

// -------------------
// Plugins
// -------------------

gradlePlugin {
    plugins {
        create("Settings Gradle Plugin") {
            id = "$groupID.$artifactId"
            implementationClass = "com.michaelflisar.kmpgradletools.SettingsFilePlugin"
        }
        isAutomatedPublishing = true
    }
}

dependencies {
}

mavenPublishing {

    configure(
        GradlePlugin(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true
        )
    )

    coordinates(
        groupId = groupID,
        artifactId = artifactId,
        version = System.getenv("TAG") ?: "LOCAL-SNAPSHOT"
    )

    pom {
        name.set(libraryName)
        description.set(libraryDescription)
        inceptionYear.set("$release")
        url.set(github)

        licenses {
            license {
                name.set(license)
                url.set(licenseUrl)
            }
        }

        developers {
            developer {
                id.set("mflisar")
                name.set("Michael Flisar")
                email.set("mflisar.development@gmail.com")
            }
        }

        scm {
            url.set(github)
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(true)

    // Enable GPG signing for all publications
    if (System.getenv("CI")?.toBoolean() == true)
        signAllPublications()
}