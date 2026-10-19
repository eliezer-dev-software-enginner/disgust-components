plugins {
    `java-library`
    `maven-publish`
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = rootProject.group
version = rootProject.version

repositories { mavenLocal(); mavenCentral() }
java { toolchain { languageVersion.set(JavaLanguageVersion.of(25)) } }
javafx {
    version = "25.0.1"
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    api(project(":"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("megalodonte:megalodonte-theme:1.0.0-beta")
}

tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
tasks.test { useJUnitPlatform() }
publishing {
    publications {
        create<MavenPublication>("web") {
            from(components["java"])
            artifactId = "disgust-web"
        }
    }
}
