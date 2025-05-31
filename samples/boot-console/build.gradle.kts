plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.boot.actuator)
    implementation(libs.boot.web)

    developmentOnly(project(":jweb-console-starter:jweb-console-spring-boot-starter"))
}