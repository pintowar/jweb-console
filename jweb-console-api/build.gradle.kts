plugins {
    id("jweb-console.publish")
    id("java-library")
}

tasks.processResources {
    val webCli = ":jweb-console-webcli"
    dependsOn("$webCli:build")

    doLast {
        val origin = project(webCli).layout.buildDirectory.get().asFile.absolutePath
        val dest = project.layout.buildDirectory.dir("resources/main/public/console").get().asFile.absolutePath
        copy {
            from(origin)
            into(dest)
        }
        logger.quiet("Cli Resources: move from $origin to $dest")
    }
}
