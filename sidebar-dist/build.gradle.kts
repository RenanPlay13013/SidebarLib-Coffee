plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow")
}

val githubRepo = System.getenv("GITHUB_REPOSITORY") ?: "githubRepo"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["shadow"])
            artifactId = "sidebar-dist"
            version = System.getenv("GITHUB_SHA") ?: "dev"
        }
    }
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepo")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")

    implementation(project(":sidebar-base"))
    implementation(project(":sidebar-v1_8_R3"))
    implementation(project(":sidebar-v1_12_R1"))
    implementation(project(":sidebar-v1_16_R3"))
    implementation(project(":sidebar-v1_17_R1"))
    implementation(project(":sidebar-v1_18_R2"))
    implementation(project(":sidebar-v1_19_R2"))
    implementation(project(":sidebar-v1_19_R3"))
    implementation(project(":sidebar-v1_20_R1"))
    implementation(project(":sidebar-v1_20_R2"))
    implementation(project(":sidebar-v1_20_R3"))
    implementation(project(":sidebar-v1_20_R4"))
    implementation(project(":sidebar-v1_21_R1"))
    implementation(project(":sidebar-v1_21_R2"))
    implementation(project(":sidebar-v1_21_R3"))
    implementation(project(":sidebar-cmn1"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }

    build {
        dependsOn(shadowJar)
    }
}
