import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version(Versions.Plugins.KOTLIN).apply(false)
    kotlin("plugin.serialization").version(Versions.Plugins.KOTLIN).apply(false)
    id("io.gitlab.arturbosch.detekt").version(Versions.Plugins.DETEKT).apply(false)
}

allprojects {
    apply<DetektPlugin>()

    repositories {
        mavenCentral()
        jcenter()
    }

    configure<DetektExtension> {
        parallel = true
        config = files("$rootDir/detekt.yml")
        buildUponDefaultConfig = false
        input = files(projectDir)
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
