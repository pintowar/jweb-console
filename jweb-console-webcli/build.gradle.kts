import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
    id("org.sonarqube")
}

description = "JWeb Console Client"

project.layout.buildDirectory.set(file("dist"))

node {
    version.set("22.16.0")
    download.set(true)
}

tasks {
    register<NpmTask>("run") {
        dependsOn(npmInstall)
        group = "application"
        description = "Run the client app"
        args.set(listOf("run", "dev"))
    }

    register<NpmTask>("build") {
        dependsOn(npmInstall)
        group = "build"
        description = "Build the client bundle"
        args.set(listOf("run", "build"))
    }

    register<NpmTask>("test") {
        dependsOn(npmInstall)
        group = "test"
        description = "unit tests"
        args.set(listOf("run", "coverage"))
    }

    register<Delete>("clean") {
        delete(project.layout.buildDirectory)
        delete("${project.projectDir}/coverage")
    }

    register("testCodeCoverageReport") {
        dependsOn("test")
        doLast {
            logger.quiet("Finishing Coverage Report!!")
        }
    }
}

sonarqube {
    properties {
        val lcovReportPath = "${projectDir.absolutePath}/coverage/"
        property("sonar.sources", "src")
        property("sonar.exclusions", "src/**/*.html,src/**/*.css,src/**/*.test.ts")
        property("sonar.inclusions", "src/**/*.ts,src/**/*.js,src/**/*.svelte")
        property("sonar.tests", "src")
        property("sonar.test.inclusions", "src/**/*.test.ts")
        property("sonar.javascript.lcov.reportPaths", "$lcovReportPath/lcov.info")
    }
}
