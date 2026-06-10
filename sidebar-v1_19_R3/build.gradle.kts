plugins {
    `java-library`
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    compileOnly("org.spigotmc:spigot:1.19.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
}
