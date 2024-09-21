package ru.otus.homework.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.GroovyCompile

class CodegenPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create('codegen', CodegenPluginExtension)
        extension.field.convention('name')
        extension.className.convention('Employee')
        extension.packageName.convention('ru.otus.homework')
        extension.outputDir.convention(
                project.layout.buildDirectory.dir(
                        "generated-source/${extension.packageName.get().replace('.', '/')}"
                )
        )

        def generate = project.tasks.register('generate', CodegenTask) {
            field.set(extension.field)
            className.set(extension.className)
            packageName.set(extension.packageName)
            outputDir.set(extension.outputDir)
        }

        project.tasks.withType(GroovyCompile).configureEach {
            dependsOn generate
        }

        project.afterEvaluate {
            (project.extensions["sourceSets"] as SourceSetContainer)["main"]
                    .java
                    .srcDir(extension.outputDir)
        }
    }
}