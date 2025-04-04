plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "2.1.10" apply true
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
repositories {
    mavenCentral()
    google()
}
dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("stdlib"))
    //noinspection UseTomlInstead
    implementation("org.smali:dexlib2:2.5.2")
    //noinspection UseTomlInstead
    implementation("org.smali:smali:2.5.2")
    //noinspection UseTomlInstead
    implementation("org.smali:baksmali:2.5.2")
    /*
    implementation(fileTree("./libs") {
        include("*.jar")
    })
     */
}
gradlePlugin {
    plugins {
        create("ApkMixin") {
            id = "momoi.plugin.apkmixin"
            implementationClass = "momoi.plugin.apkmixin.MixinPlugin"
        }
    }
}