plugins {
    kotlin("jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("com.github.haifengl:smile-kotlin:3.0.1")
    implementation("com.github.haifengl:smile-core:3.0.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(17)
}