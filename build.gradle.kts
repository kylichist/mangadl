plugins {
    kotlin("jvm") version "1.4.20"
}

group = "com.github.kylichist"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.13.1")
}