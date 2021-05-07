enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot
    val springBootVersion = "2.4.4"
    // https://mvnrepository.com/artifact/io.spring.dependency-management/io.spring.dependency-management.gradle.plugin
    val springDependencyManagementVersion = "1.0.11.RELEASE"
    val kotlinVersion = "1.4.31"

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
    }
}


dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../libs.dispatcher.toml"))
        }
    }
}

rootProject.name = "dispatcher"
