plugins {
    `java-library`
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-v1_20_R3"))
    implementation("org.jetbrains:annotations:24.0.1")
}
