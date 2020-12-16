plugins {
    kotlin("jvm") version "1.4.21"
}

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.github.kylichist"
    version = "1.0-SNAPSHOT"
}