import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.pad.shapeless.shared"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(dispatcher.kotlin.stdlib.jdk8)
    implementation(dispatcher.kotlin.reflect)
    implementation(dispatcher.kotlin.noarg)
    implementation(dispatcher.jackson)
    implementation(dispatcher.spring.boot.starter.websocket)
}