plugins {
    java
    jacoco
    id("com.diffplug.spotless")
}

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    testImplementation(libs.bundles.tests)
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        leadingTabsToSpaces()
        importOrder()
        formatAnnotations()
        endWithNewline()
    }
}

tasks {
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        archiveExtension.set("jar")
        from(sourceSets["main"].allSource)
    }

    register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        archiveExtension.set("jar")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

// Do not generate reports for individual projects
tasks.jacocoTestReport {
    enabled = false
}