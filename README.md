# JWeb Console

![master status](https://github.com/pintowar/jweb-console/actions/workflows/master.yml/badge.svg?branch=master)
[![Sonar Coverage](https://sonarcloud.io/api/project_badges/measure?project=pintowar_jweb-console&metric=coverage)](https://sonarcloud.io/dashboard?id=pintowar_jweb-console)

![develop status](https://github.com/pintowar/jweb-console/actions/workflows/develop.yml/badge.svg?branch=develop)
[![Sonar Coverage](https://sonarcloud.io/api/project_badges/measure?project=pintowar_jweb-console&metric=coverage&branch=develop)](https://sonarcloud.io/dashboard?id=pintowar_jweb-console&branch=develop)

![GitHub tag (latest)](https://img.shields.io/github/v/tag/pintowar/jweb-console)
![GitHub license](https://img.shields.io/github/license/pintowar/jweb-console)

# Overview

This application is intended for embedding a web scripting console (for groovy, jruby or kotlin) in a java web application (Micronaut/Spring Boot)
which then could access the application context (for development purposes).

![image](https://user-images.githubusercontent.com/354264/233759240-456abea7-31b5-4147-9173-8fed51164f53.png)

# Usage

Add framework starter dependency and enabled it on the framework config file.

To add the dependency add the following dependency to each framework:

## Frameworks

JWeb Console can be used in different web frameworks add the dependency using the following config:

### For Spring Boot (>= 3.0.0)

```kotlin
runtimeOnly("io.github.pintowar:jweb-console-spring-boot-starter:x.y.z")
```

### For Micronaut (>= 3.5.0)

```kotlin
runtimeOnly("io.github.pintowar:jweb-console-micronaut-starter:x.y.z")
```

## Applying config

This lib is meant to be used only in development environments. Therefore, preferably these settings should be done in `application-dev.properties` or `application-dev.yml` files.

Add the `jweb.console.enabled=true` config to the application config (preferably only on dev environment).

After the application is started, the console can be accessed on the url: `http://localhost:8080/console`

Sample applications can be found on the `samples` folder.
