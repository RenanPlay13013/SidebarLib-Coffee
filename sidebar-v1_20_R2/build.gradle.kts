plugins {
    `java-library`
}

dependencies {
    compileOnly(project(":sidebar-base"))
    compileOnly("org.spigotmc:spigot:1.20.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
}
