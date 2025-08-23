plugins {
    kotlin("jvm") version "2.1.20"
}

group = "com.pedrodev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project("lexer"))
    implementation(project("parser"))
    implementation(project("vm"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}