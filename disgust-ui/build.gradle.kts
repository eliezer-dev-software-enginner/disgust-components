plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = rootProject.group
version = rootProject.version
repositories {
    mavenLocal()
    mavenCentral()
}
java { toolchain { languageVersion.set(JavaLanguageVersion.of(25)) } }
javafx {
    version = "25.0.1"
    modules("javafx.controls", "javafx.graphics")
}
dependencies {
    implementation(project(":"))
    implementation("megalodonte:megalodonte-theme:1.0.0-beta")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application { mainClass.set("disgust.ui.Main") }
tasks.test { useJUnitPlatform() }

tasks.named<JavaExec>("run") {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
