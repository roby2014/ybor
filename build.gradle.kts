import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.example"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.0")
}

tasks.test { useJUnitPlatform() }

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

application { mainClass.set("MainKt") }
