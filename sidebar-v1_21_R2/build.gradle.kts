plugins {
    `java-library`
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly(project(":sidebar-base"))
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation(project(":sidebar-cmn1"))
}
