plugins {
    `java-library`
    id("jweb-console.publish")
}

dependencies {
    api(project(":jweb-console-api"))
    implementation(libs.boot.web)
    allLangSubModules.forEach(::runtimeOnly)

    annotationProcessor(libs.bundles.boot.config.processors)

    testImplementation(libs.boot.test)
}

