plugins {
    kotlin("jvm")
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:${Versions.Dependencies.KTOR}"))
    implementation(platform("io.micrometer:micrometer-bom:${Versions.Dependencies.MICROMETER}"))
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))

    api(project(":imgur"))
    api("io.micrometer:micrometer-core")
    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-client-serialization")
    implementation("io.ktor:ktor-client-logging")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")
}
