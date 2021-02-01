plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("org.json:json:20201115")
}