plugins {
    kotlin("jvm") version "2.2.20"
    application
    id("io.github.rascmatt.z3") version "1.0.2"
}

group = "dev.yort"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // For z3
    maven {
        url = uri("https://artifacts.itemis.cloud/repository/maven-mps/")
    }

}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "dev.yort.MainKt"
}
