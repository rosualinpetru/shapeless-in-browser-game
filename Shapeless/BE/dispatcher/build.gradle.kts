import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.jpa")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.pad.shapeless.dispatcher"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(dispatcher.jjwt)
    implementation(dispatcher.jackson)
    implementation(dispatcher.postgres)
    implementation(dispatcher.kotlin.reflect)
    implementation(dispatcher.kotlin.stdlib.jdk8)
    implementation(dispatcher.kotlin.noarg)
    implementation(dispatcher.spring.boot.starter.web)
    implementation(dispatcher.spring.boot.starter.data.jpa)
    implementation(dispatcher.spring.boot.starter.security)
    implementation(dispatcher.spring.boot.starter.validation)
    implementation(dispatcher.spring.boot.starter.websocket)
    implementation(dispatcher.spring.boot.starter.oauth2.client)
    implementation(dispatcher.spring.security.messaging)
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("dispatcher.${archiveExtension.get()}")
}

