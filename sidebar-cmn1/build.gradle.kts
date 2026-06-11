plugins {
    `java-library`
}

description = "NMS version commons 1. For 1.20 and 1.21 where server version was removed from package name."

dependencies {
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    implementation("org.jetbrains:annotations:24.0.1")

}
