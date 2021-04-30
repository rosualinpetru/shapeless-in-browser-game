import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
}

subprojects {
    group = "com.pad.dispatcher"
    version = "0.0.1"

    apply(plugin = "java")

    java.sourceCompatibility = JavaVersion.VERSION_11

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}

fun cleanTasks(): List<String> {
    return subprojects.mapNotNull {
        it.tasks.findByPath("clean")?.path
    }
}

tasks.getByPath("clean").dependsOn(cleanTasks())

