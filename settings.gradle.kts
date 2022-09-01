pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven ( "https://dl.bintray.com/kotlin/ktor" )
        maven ( "https://dl.bintray.com/kotlin/kotlinx" )
        maven ( "https://kotlin.bintray.com/kotlinx" )
        maven("https://repo.maven.apache.org/maven2")
        maven("https://repo1.maven.org/maven2/")
        maven("https://jitpack.io")
        maven("https://mvnrepository.com/artifact")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:7.0.4")
            }
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "rocket.chat.kmm"

