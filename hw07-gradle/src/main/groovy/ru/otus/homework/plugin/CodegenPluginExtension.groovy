package ru.otus.homework.plugin

import org.gradle.api.provider.Property
import org.gradle.api.file.DirectoryProperty

interface CodegenPluginExtension {
    Property<String> getField()
    Property<String> getClassName()
    Property<String> getPackageName()
    DirectoryProperty getOutputDir()
}