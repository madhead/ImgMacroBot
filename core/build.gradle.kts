plugins {
    kotlin("jvm")
}

dependencies {
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))
    testImplementation(platform("org.junit:junit-bom:${Versions.Dependencies.JUNIT}"))
    testRuntimeOnly(platform("org.junit:junit-bom:${Versions.Dependencies.JUNIT}"))

    implementation(project(":imgur"))
    implementation("dev.inmo:tgbotapi:${Versions.Dependencies.TGBOTAPI}")
    implementation("org.jetbrains.skija:skija-linux:${Versions.Dependencies.SKIJA}")
    implementation("org.apache.logging.log4j:log4j-core")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk:mockk:${Versions.Dependencies.MOCKK}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
