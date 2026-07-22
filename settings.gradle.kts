pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MidMoney"
include(":app")
include(":library:common")
include(":library:error")
include(":library:designsystem")
include(":library:logger")
include(":library:network")
include(":library:remoteconfig")
include(":library:datastore")
include(":library:navigation")
include(":library:mvi")
include(":library:websocket")
include(":library:event")
include(":plugin:market")
include(":plugin:news")
include(":plugin:user")

include(":feature:login")
include(":feature:market")
include(":feature:marketlist")
include(":feature:detail")
include(":feature:watchlist")
include(":feature:profile")
 