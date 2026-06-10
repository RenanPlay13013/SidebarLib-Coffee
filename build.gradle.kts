plugins {
    id("com.gradleup.shadow") version "9.4.2" apply false
}

group = "com.andrei1058.spigot.sidebar"
version = "25.2.2-SNAPSHOT"

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.codemc.io/repository/nms/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://hub.spigotmc.org/nexus/repository/public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

subprojects {

    apply(plugin = "java")

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
