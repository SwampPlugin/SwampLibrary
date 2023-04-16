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
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.19.3-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.19.3-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
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
        destinationDirectory.set(File("D:\\서버\\1.19.3 - 악어 놀이터2\\plugins"))
    }
}