plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.gradlex.extra-java-module-info") version "1.14.2"
}

group = rootProject.group
version = rootProject.version
repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
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
extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
    automaticModule("com.github.eliezer-dev-software-enginner:pack-utilities", "pack.utilities")
}
application { mainClass.set("disgust.ui.Main") }
tasks.test { useJUnitPlatform() }

tasks.named<JavaExec>("run") {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
