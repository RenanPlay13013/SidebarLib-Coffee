plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.4"
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")

    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
}
