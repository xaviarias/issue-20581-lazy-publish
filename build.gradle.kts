import org.gradle.api.plugins.internal.DefaultAdhocSoftwareComponent

plugins {
    java
    `idea`
    `maven-publish`
//    `ivy-publish`
    kotlin("jvm") version "1.7.21"
}

repositories {
    mavenCentral()
}

group = "org.example"
version = "1.0-SNAPSHOT"

sourceSets {
    create("feature") {
        kotlin {
            srcDir("src/feature/kotlin")
        }
    }
}

java {
    registerFeature("testFeature") {
        usingSourceSet(sourceSets["feature"])
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    "featureImplementation"("org.slf4j:slf4j-jdk14:1.7.5")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            suppressAllPomMetadataWarnings()
            from(components["java"])
        }
//        create<IvyPublication>("ivy") {
//            organisation = "org.example"
//            module = "issue-20581"
//            revision = "1.1"
//            from(components["kotlin"])
//        }
    }
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

val pub = publishing.publications["maven"] as MavenPublication
println("\nMaven artifacts:")
pub.artifacts.onEach { println(it.file.relativeTo(project.projectDir)) }

// Skip feature configurations
val javaComp = components["java"] as DefaultAdhocSoftwareComponent
javaComp.withVariantsFromConfiguration(configurations["featureApiElements"]) { skip() }
javaComp.withVariantsFromConfiguration(configurations["featureRuntimeElements"]) { skip() }

println("\nGMM artifacts:")
javaComp.usages.flatMap {
    it.artifacts
}.map {
    it.file
}.distinct().onEach {
    println(it.relativeTo(project.projectDir))
}
