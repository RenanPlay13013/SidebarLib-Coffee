plugins {
    id("com.gradleup.shadow") version "9.4.2" apply false
    id("io.papermc.paperweight.userdev") version "1.7.4" apply false
}

group = "com.andrei1058.spigot.sidebar"
version = findProperty("version") as String? ?: "25.2.2-SNAPSHOT"

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://hub.spigotmc.org/nexus/repository/public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

subprojects {

    apply(plugin = "java-library")

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Javadoc> {
        enabled = false
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
