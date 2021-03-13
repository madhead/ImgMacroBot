import java.lang.System.getenv as env

plugins {
    kotlin("jvm")
    id("org.liquibase.gradle")
}

dependencies {
    implementation(platform("org.apache.logging.log4j:log4j-bom:${Versions.Dependencies.LOG4J}"))
    implementation(platform("io.micrometer:micrometer-bom:${Versions.Dependencies.MICROMETER}"))
    testImplementation(platform("org.junit:junit-bom:${Versions.Dependencies.JUNIT}"))
    testRuntimeOnly(platform("org.junit:junit-bom:${Versions.Dependencies.JUNIT}"))

    api(project(":imgur"))
    api("io.micrometer:micrometer-core")
    api("dev.inmo:tgbotapi:${Versions.Dependencies.TGBOTAPI}")
    api("org.jetbrains.skija:skija-linux:${Versions.Dependencies.SKIJA}")
    implementation("org.apache.logging.log4j:log4j-core")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.mockk:mockk:${Versions.Dependencies.MOCKK}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    liquibaseRuntime("org.liquibase:liquibase-core:${Versions.Dependencies.LIQUIBASE}")
    liquibaseRuntime("org.yaml:snakeyaml:${Versions.Dependencies.SNAKEYAML}")
    liquibaseRuntime("com.oracle.database.jdbc:ojdbc11-production:${Versions.Dependencies.OJDBC11}")
}

liquibase {
    activities {
        register("imgmacrobot") {
            this.arguments = mapOf(
                "url" to "jdbc:oracle:thin:@${env("DB_TNSNAME")}?TNS_ADMIN=${project.file("src/main/wallet").absolutePath}",
                "username" to env("DB_USER"),
                "password" to env("DB_PASSWORD"),
                "driver" to "oracle.jdbc.OracleDriver",
                "changeLogFile" to "src/main/liquibase/changelog.yml",
                "classpath" to "$projectDir"
            )
        }
    }
}
