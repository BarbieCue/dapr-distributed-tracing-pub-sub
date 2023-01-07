plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
}

application {
    mainClass.set("com.example.ApplicationKt")
}


ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}

repositories {
    mavenCentral()
}

allprojects {
    tasks {
        task("buildAllServices") {
            dependsOn("buildFatJar")
        }
    }
}