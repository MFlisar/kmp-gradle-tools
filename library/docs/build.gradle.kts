import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
}

// -------------------
// Informations
// -------------------

val description = "functions to build mkdocs documentation for kmp projects"

// Module
val artifactId = "docs"

// Library
val libraryName = "kmp-gradle-tools"
val libraryDescription = "$libraryName - $artifactId module - $description"
val groupID = "io.github.mflisar.kmp-gradle-tools"
val release = 2025
val github = "https://github.com/MFlisar/kmp-gradle-tools"
val license = "Apache License 2.0"
val licenseUrl = "$github/blob/main/LICENSE"

// -------------------
// Setup
// -------------------

dependencies {
    implementation(deps.ktoml.core)
    implementation(deps.ktoml.file)
    implementation(deps.jsoup)
    implementation(deps.yaml)

    implementation(project(":shared"))
}

// allows to run the application with `./gradlew run -PmainClass=com.michaelflisar.scripts.UpdateDocsKt`
if (System.getenv("CI")?.toBoolean() == true) {
    application {
        val mc = project.findProperty("mainClass") as? String
        if (mc != null) {
            mainClass.set(mc)
        }
    }
}

// -------------------
// Configurations
// -------------------

mavenPublishing {

    configure(
        JavaLibrary(
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