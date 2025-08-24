plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "portugol-studio-compiler"
include(":lexer", ":parser", ":vm")

project(":lexer").projectDir = file("lexer")
project(":parser").projectDir = file("parser")
project(":vm").projectDir = file("vm")
