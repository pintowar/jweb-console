import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.isSnapshotVersion: Boolean
    get() = version.toString().endsWith("SNAPSHOT")

val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

val Project.allLangSubModules: List<Project>
    get() = this.rootProject
            .subprojects
            .filter { it.plugins.hasPlugin("jweb-console.base") }
            .filter { it.path.startsWith(":jweb-console-lang:") }

val Project.allStarterSubModules: List<Project>
    get() = this.rootProject
            .subprojects
            .filter { it.path.startsWith(":jweb-console-starter:") }
