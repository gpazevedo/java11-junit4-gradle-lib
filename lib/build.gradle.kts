plugins {
    // Apply the java-library plugin for API and implementation separation.
    id ("br.com.sbf.dataplatform.beam.common-conventions")
    alias(libs.plugins.lombok)
    alias(libs.plugins.google)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.properties["group"].toString()
            version = project.properties["version"].toString()
            artifactId = "lib"

            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("artifactregistry://us-maven.pkg.dev/prod-data-platform-297218/data-platform-java")
        }
        maven {
            url = uri("artifactregistry://us-maven.pkg.dev/dev-data-platform-291914/data-platform-java")
        }
    }
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.8.0")

    implementation(libs.slf4j.api)
    testImplementation(libs.slf4j.simple)

    implementation(libs.beam.java)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed")
    }
}
tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

