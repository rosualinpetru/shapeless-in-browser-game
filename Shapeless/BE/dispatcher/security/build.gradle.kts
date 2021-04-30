plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.client)
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