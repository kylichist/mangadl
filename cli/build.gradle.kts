plugins {
    kotlin("jvm")
}

sourceSets {
    val mangakakalot by creating {
        java {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output
        }
    }
}

val mangakakalotJar = task("mangakakalotJar", type = Jar::class) {
    group = "application"
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "MangaKakalot CLI"
        attributes["Implementation-Version"] = "todo"
        attributes["Main-Class"] = "com.github.mangadl.cli.mangakakalot.main.kt"
    }
    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) } )
    with(tasks["jar"] as CopySpec)
}

/*
tasks {
    "build" {
        dependsOn(mangakakalotJar)
    }
}*/
