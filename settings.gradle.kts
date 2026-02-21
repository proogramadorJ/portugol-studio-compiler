plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "portugol-studio-compiler"
include(":lexer", ":parser", ":semantic",":vm", ":bytecode", "commons")

project(":lexer").projectDir = file("lexer")
project(":parser").projectDir = file("parser")
project(":semantic").projectDir = file("semantic")
project(":vm").projectDir = file("vm")
project(":bytecode").projectDir = file("bytecode")
project(":commons").projectDir = file("commons")
