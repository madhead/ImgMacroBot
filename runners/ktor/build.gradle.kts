plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:${Versions.Dependencies.KTOR}"))
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))

    implementation("io.ktor:ktor-server-netty")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")
}

application {
    applicationName = rootProject.name
    mainClass.set("io.ktor.server.netty.EngineMain")
}
