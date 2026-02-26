plugins {
    kotlin("jvm")
}

group = "com.pedrodev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":parser"))
    implementation(project(":lexer"))
    implementation(project(":semantic"))
    implementation(project(":commons"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}