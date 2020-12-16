plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

dependencies {
    implementation(project(":common"))
    implementation("org.jsoup:jsoup:1.13.1")
}

sourceSets {
    val jar by creating {
        java {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "com.github.kylichist.mangadl.mangakakalot.MainKt.kt",
                "Implementation-Title" to "MangaKakalot CLI",
                //TODO
                "Implementation-Version" to "TODO!!"
            )
        )
    }
}