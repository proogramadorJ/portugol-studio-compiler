plugins {
    kotlin("multiplatform") version "2.1.20" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.android.application") version "8.2.2" apply false
    kotlin("plugin.compose") version "2.1.20" apply false
}

allprojects {
    group = "com.pedrodev"
    version = "1.0-SNAPSHOT"

    repositories {
        google()
        mavenCentral()
    }
}
