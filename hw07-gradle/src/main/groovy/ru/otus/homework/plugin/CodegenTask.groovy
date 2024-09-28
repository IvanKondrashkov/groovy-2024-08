package ru.otus.homework.plugin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.file.DirectoryProperty

abstract class CodegenTask extends DefaultTask {
    @Input
    abstract Property<String> getField()
    @Input
    abstract Property<String> getClassName()
    @Input
    abstract Property<String> getPackageName()
    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @TaskAction
    def compile() {
        def field = getField().get()
        def className = getClassName().get()
        def packageName = getPackageName().get()

        def dir = getOutputDir().get().asFile
        def srcFile = new File(dir, "${className}.groovy")

        if (!srcFile.exists()) {
            srcFile << """\
            package $packageName
            
            class $className {
                String $field
            }
        """.stripIndent()
        }
    }
}