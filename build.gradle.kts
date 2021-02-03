import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version(Versions.Plugins.KOTLIN).apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = Versions.JVM
        }
        withType<Test> {
            useJUnitPlatform()
            testLogging {
                showStandardStreams = true
            }
        }
    }
}
