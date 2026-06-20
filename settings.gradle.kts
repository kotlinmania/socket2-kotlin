pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Include libc-kotlin from sibling directory for local development
includeBuild("../libc-kotlin")

rootProject.name = "socket2-kotlin"
