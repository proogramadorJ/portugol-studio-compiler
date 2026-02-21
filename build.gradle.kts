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
    implementation(project("semantic"))
    implementation(project("vm"))
    implementation(project("bytecode"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}