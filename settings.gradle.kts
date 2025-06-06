rootProject.name = "jweb-console"

include("jweb-console-api", "jweb-console-webcli")
include("jweb-console-lang:groovy", "jweb-console-lang:kotlin", "jweb-console-lang:jruby")
include("jweb-console-starter:spring-boot", "jweb-console-starter:micronaut")
include("samples:boot-console", "samples:mn-console")

project(":jweb-console-lang:groovy").name = "jweb-console-lang-groovy"
project(":jweb-console-lang:kotlin").name = "jweb-console-lang-kotlin"
project(":jweb-console-lang:jruby").name = "jweb-console-lang-jruby"

project(":jweb-console-starter:spring-boot").name = "jweb-console-spring-boot-starter"
project(":jweb-console-starter:micronaut").name = "jweb-console-micronaut-starter"