plugins {
    `java-library`
    id("jweb-console.publish")
}

dependencies {
    implementation(project(":jweb-console-api"))
    api(libs.jruby.base)
}
