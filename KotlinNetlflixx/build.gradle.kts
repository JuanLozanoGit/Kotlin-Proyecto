plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.haifengl:smile-kotlin:3.0.1")
    implementation("org.apache.commons:commons-csv:1.10.0")
    implementation("com.opencsv:opencsv:5.7.1")
}

application {
    mainClass.set("MainKt")
}
