import com.mineinabyss.kotlinSpice
import com.mineinabyss.mineInAbyss
import com.mineinabyss.sharedSetup

plugins {
    java
    idea
    `maven-publish`
    kotlin("jvm")
    id("com.mineinabyss.shared-gradle") version "0.0.6"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.github.slimjar") version "1.2.0"
}

sharedSetup()

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://erethon.de/repo/")
    mineInAbyss()
}

val kotlinVersion: String by project
val serverVersion: String by project

dependencies {
    compileOnly("org.spigotmc:spigot-api:$serverVersion")
    slim(kotlin("stdlib-jdk8"))
    slim("de.erethon:headlib:3.0.6")

    implementation("com.google.protobuf:protobuf-java:3.8.0")
    implementation("com.google.dagger:dagger:2.28.1")
    compileOnly("org.jetbrains:annotations:17.0.0")

    testImplementation("junit:junit:4.12")

    annotationProcessor("com.google.dagger:dagger-compiler:2.28.1")
}
