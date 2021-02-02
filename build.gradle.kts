import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version(Versions.Plugins.KOTLIN).apply(false)
}

allprojects {
    repositories {
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
