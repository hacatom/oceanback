plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.github.ityeri.oceanwiki"
version = "v0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project.libs.bundles.ktor)
    implementation(project.libs.bundles.exposed)
    implementation(project.libs.mysqlConnector)
    implementation(project.libs.koinCore)
    implementation(project.libs.bundles.logging)
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
}

kotlin {
    jvmToolchain(20)
}