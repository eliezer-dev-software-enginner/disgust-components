plugins {
    id("java-library")
    id("maven-publish")
    id("org.gradlex.extra-java-module-info") version "1.14.2"

    // 🛑 CORREÇÃO: Usando o ID e a versão CORRETOS conforme a documentação oficial.
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "disgust"
version = "1.0.0-beta"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}


// 🛑 2. CONFIGURA O PLUGIN DO JAVAFX
javafx {
    // Define a versão do JavaFX para ser usada em todos os módulos
    version = "25.0.1" // Mantida a versão 17.0.10.

    // Lista os módulos JavaFX que sua biblioteca PRECISA para compilar.
    // O plugin adiciona automaticamente a dependência para a sua plataforma de build.
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    testImplementation("megalodonte:megalodonte-theme:1.0.0-beta")
    // Dependências de teste (mantidas)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    
    // TestFX for JavaFX testing
    testImplementation("org.testfx:testfx-core:4.0.16-alpha")
    testImplementation("org.testfx:testfx-junit5:4.0.16-alpha")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")

    // Megalodonte
    api("megalodonte:megalodonte-base:1.0.0-beta")
    api("megalodonte:megalodonte-components:1.0.0-beta")
    api("megalodonte:megalodonte-reactivity:1.0.0-beta")
    implementation("megalodonte:megalodonte-router:1.0.0-beta")

    //utilitties
    implementation("com.github.eliezer-dev-software-enginner:pack-utilities:v1.0.0")

    api(project(":disgust-icons"))
}


tasks.test {
    useJUnitPlatform()
}

tasks.named<Test>("test") {
    dependsOn(tasks.named("jar"))
}

task<JavaExec>("runDemo") {
    group = "application"
    description = "Run the MenuDemo"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("megalodonte.components.MenuDemo")
}

tasks.jar {
    archiveBaseName.set("distust-io-components")

    manifest {
        attributes(
            "Implementation-Title" to "Megalodonte Components Library",
            "Implementation-Version" to project.version
        )
    }
}

// Configuração de Publicação (mantida)
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "distust-io-components"
        }
    }
}

// The published utilities JAR lacks module metadata and its v1.0.0 filename
// cannot yield a valid automatic module name.
extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
    automaticModule("com.github.eliezer-dev-software-enginner:pack-utilities", "pack.utilities")
}
