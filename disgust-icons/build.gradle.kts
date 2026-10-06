plugins {
    `java-library`
    `maven-publish`
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = rootProject.group
version = rootProject.version
repositories {
    mavenCentral()
    mavenLocal()
}
dependencies { api("megalodonte:megalodonte-base:1.0.0-beta") }
java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(25)) }
    withSourcesJar()
}
javafx {
    version = "25.0.1"
    modules("javafx.controls", "javafx.graphics")
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "disgust-icons"
        }
    }
}
