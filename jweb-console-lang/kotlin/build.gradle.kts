plugins {
    `java-library`
    id("jweb-console.publish")
}

dependencies {
    implementation(project(":jweb-console-api"))
    implementation(libs.kotlin.jsr223)
}
