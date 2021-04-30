plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":service"))
    implementation(project(":dto"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
