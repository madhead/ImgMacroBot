plugins {
    kotlin("jvm")
}

dependencies {
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))

    implementation("dev.inmo:tgbotapi:${Versions.Dependencies.TGBOTAPI}")
    implementation("org.apache.logging.log4j:log4j-core")
}
