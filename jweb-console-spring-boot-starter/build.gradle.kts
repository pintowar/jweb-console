plugins {
    `java-library`
    id("jweb-console.publish")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom(libs.boot.dependencies.get().toString())
    }
}

dependencies {
    api(project(":jweb-console-api"))
    implementation(libs.boot.web)
    allLangSubModules.forEach(::runtimeOnly)

    annotationProcessor(libs.bundles.boot.config.processors)
}

