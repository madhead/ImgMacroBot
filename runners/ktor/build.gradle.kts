plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:${Versions.Dependencies.KTOR}"))
    implementation(platform("io.micrometer:micrometer-bom:${Versions.Dependencies.MICROMETER}"))
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))

    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-metrics-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.koin:koin-ktor:${Versions.Dependencies.KOIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Dependencies.KOTLINX_SERIALIZATION}")
    implementation("dev.inmo:tgbotapi:${Versions.Dependencies.TGBOTAPI}")
    implementation(project(":imgur:ktor"))
    implementation(project(":core"))
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")
}

application {
    applicationName = rootProject.name
    mainClass.set("io.ktor.server.netty.EngineMain")
}
