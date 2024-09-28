import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import org.gradle.testkit.runner.GradleRunner

class CodegenPluginFunctionalTest {
    @TempDir
    public File testProjectDir
    File buildFile

    @BeforeEach
    void init() {
        buildFile = new File(testProjectDir, 'build.gradle')
        buildFile << """\
            plugins {
                id 'groovy'
                id 'ru.otus.homework.plugin' apply true
            }
            
            repositories {
                mavenCentral()
            }

            codegen {
                field = 'name'
                className = 'Employee'
                packageName = 'ru.otus.homework'
            }
        """.stripIndent()
    }

    @AfterEach
    void tearDown() {
        buildFile.delete()
    }


    @Test
    void generateTask() {
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments('build')
                .withPluginClasspath()
                .build()

        assert result.task(":generate").outcome.name() == "SUCCESS"

        def filePath = "build/generated-source/ru/otus/homework/Employee.groovy"
        def file = new File(testProjectDir, filePath)
        def clazz = new GroovyClassLoader().parseClass(file)

        assert clazz.simpleName == "Employee"
        assert clazz.packageName == "ru.otus.homework"
    }
}