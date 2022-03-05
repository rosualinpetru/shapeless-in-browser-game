import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.pad.shapeless.designer"
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
    implementation(designer.jackson)
    implementation(designer.kotlin.reflect)
    implementation(designer.kotlin.stdlib.jdk8)
    implementation(designer.spring.boot.starter.web)
    implementation(designer.spring.boot.starter.websocket)
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("designer.${archiveExtension.get()}")
}

