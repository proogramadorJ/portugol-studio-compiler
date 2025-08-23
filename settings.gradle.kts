plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "portugol-studio-compiler"
include("lexer")
include("parser")
include("vm")