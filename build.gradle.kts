import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.vitalijreicherdt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.zeromq:jeromq:0.5.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.register<JavaExec>("runServer") {
    classpath = sourceSets["main"].runtimeClasspath
    main = "HelloWorldServerKt"
}

//tasks.register<Jar>("HelloServerJar") {
//    archiveClassifier.set("HelloServer")
//    manifest {
//        attributes["Main-Class"] = "HelloWorldServerKt"
//    }
//
//    from(sourceSets.main.get().output)
//
//    dependsOn(configurations.runtimeClasspath)
//    from({
//        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//    })
//}

val jar by tasks.getting(Jar::class) {
    archiveBaseName.set("ZMQHelloServer")
    manifest {
        attributes["Main-Class"] = "HelloWorldServerKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
