plugins {
    application
    id("java")
    id("war")
    id("org.liquibase.gradle") version "2.2.2"
}

group = "com.ylab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("com.aston.personal_book_library.config.AppConfig")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.glassfish.jersey.containers:jersey-container-servlet:4.0.0-M1")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:4.0.0-M1")
    implementation("org.glassfish.jersey.inject:jersey-cdi2-se:4.0.0-M1")
    implementation("org.jboss.weld.se:weld-se-core:6.0.0.Beta1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.postgresql:postgresql:42.7.5")

    implementation("org.liquibase:liquibase-core:4.31.0")

    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation ("org.mockito:mockito-junit-jupiter:5.16.1")


    liquibaseRuntime("org.liquibase:liquibase-core:4.31.0")
    liquibaseRuntime("org.postgresql:postgresql:42.7.5")
    liquibaseRuntime("info.picocli:picocli:4.7.6")

    implementation("org.mindrot:jbcrypt:0.4")
    implementation ("ch.qos.logback:logback-classic:1.5.11")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to "src/main/resources/db/migration/changelog.xml",
            "url" to "jdbc:postgresql://localhost:5433/personal_book_library_db",
            "username" to "ivan",
            "password" to "secret",
        )
    }
    runList = "main"
}

tasks.register("dev") {
    dependsOn("update")
}