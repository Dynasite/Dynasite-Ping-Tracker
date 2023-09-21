@file:Suppress("VulnerableLibrariesLocal")

plugins {
    id("java")
    id("application")
    id("idea")
}

group = "org.site"
var entryClass = "Main"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("$group.$entryClass")
}

repositories {
    mavenCentral()
}

dependencies {
    // Webserver Framework
    // TODO: DO NOT FORGET: VULNERABILITIES
    implementation("org.nanohttpd:nanohttpd:2.3.0")

    // Logging Framework
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.jetbrains:annotations:24.0.0")

    //JUnit Testing Framework
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    //Mailing Framework
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("org.quartz-scheduler:quartz:2.3.2")

    //Json data
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}