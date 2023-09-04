@file:Suppress("VulnerableLibrariesLocal")

plugins {
    id("java")
    id("application")
    id("idea")
}

group = "org.dynasite"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("$group.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.nanohttpd/nanohttpd
    // TODO: DO NOT FORGET: VULNERABILITIES
    implementation("org.nanohttpd:nanohttpd:2.3.0")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}