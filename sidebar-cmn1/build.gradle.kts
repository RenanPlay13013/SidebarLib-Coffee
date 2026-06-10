plugins {
    `java-library`
}

description = "NMS version commons 1. For 1.20 and 1.21 where server version was removed from package name."

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    compileOnly("org.spigotmc:spigot:1.21.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
}
