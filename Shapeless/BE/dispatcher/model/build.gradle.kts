plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(libs.spring.boot.starter.data.jpa)
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

noArg {
    annotation("javax.persistence.Entity")
}