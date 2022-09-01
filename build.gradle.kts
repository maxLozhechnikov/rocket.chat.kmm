plugins {
    kotlin("multiplatform") version("1.7.10")
    kotlin("plugin.serialization") version("1.7.10")
    id("maven-publish")
    id("com.android.library")
}

group = "com.omega"
version = "0.0.1"
project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenLocal") {
                artifactId = "rocket.chat.kmm"
                groupId = "com.omega"
                version = "0.0.1"
                from(components["kotlin"])
            }
        }
    }
}

repositories {
    google()
    jcenter()
    mavenCentral()
    mavenLocal()
}

kotlin {
    val ktor_version = "2.1.0"
    val kotlinx_version = "1.4.0"
    val coroutines_version = "1.6.4"
    android {
        publishLibraryVariants("release", "debug")
    }
    iosX64 {
        binaries {
            framework {
                transitiveExport = true
                baseName = "library"
            }
        }
    }
    iosArm64 {
        binaries {
            framework {
                transitiveExport = true
                baseName = "library"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_version")

                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

                implementation("io.ktor:ktor-client-logging:$ktor_version")

                implementation("io.ktor:ktor-network:$ktor_version")
                implementation("io.ktor:ktor-websockets:$ktor_version")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                rootProject
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                implementation("io.ktor:ktor-client-android:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_version")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-iosx64:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-iosx64:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-iosx64:$coroutines_version")
            }
        }
        val iosX64Test by getting
        val iosArm64Main by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-iosarm64:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-iosarm64:$kotlinx_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-iosarm64:$coroutines_version")
            }
        }
        val iosArm64Test by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktor_version")
            }
        }
        val iosTest by creating {
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(31)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    lintOptions {
        isAbortOnError = false
    }
}