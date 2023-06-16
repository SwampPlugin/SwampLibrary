plugins {
    id("maven-publish")
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.cosine.library"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.19.3-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "5.0.0")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.cosine.library"
            artifactId = "SwampLibrary"
            version = "1.0"

            from(components["java"])
        }
    }
}

tasks {
    shadowJar {
        //relocate("kotlin.reflect", "com.cosine.kotlin.reflect")
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        destinationDirectory.set(File("D:\\서버\\1.19.3 - 마크존\\plugins"))
    }
}