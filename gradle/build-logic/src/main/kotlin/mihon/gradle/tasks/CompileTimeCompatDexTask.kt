package mihon.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject
import javax.tools.DiagnosticCollector
import javax.tools.JavaFileObject
import javax.tools.ToolProvider

abstract class CompileTimeCompatDexTask : DefaultTask() {

    @get:Inject
    abstract val execOperations: ExecOperations

    @get:InputDirectory
    abstract val sourceDir: DirectoryProperty

    @get:InputFiles
    abstract val compileClasspath: ConfigurableFileCollection

    @get:InputFiles
    abstract val bootClasspath: ConfigurableFileCollection

    @get:Input
    abstract val buildToolsVersion: Property<String>

    @get:InputDirectory
    abstract val sdkDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun action() {
        val srcDir = sourceDir.get().asFile
        val outDir = outputDir.get().asFile
        outDir.mkdirs()

        val tempClasses = File(temporaryDir, "classes").apply {
            deleteRecursively()
            mkdirs()
        }
        val tempDex = File(temporaryDir, "dex").apply {
            deleteRecursively()
            mkdirs()
        }

        val javaFiles = project.fileTree(srcDir) {
            include("**/*.java")
        }.files

        if (javaFiles.isEmpty()) return

        val cp = (compileClasspath.files + bootClasspath.files).joinToString(File.pathSeparator) { it.absolutePath }

        val compiler = ToolProvider.getSystemJavaCompiler()
        if (compiler != null) {
            val diagnostics = DiagnosticCollector<JavaFileObject>()
            val fileManager = compiler.getStandardFileManager(diagnostics, null, null)
            val compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles)
            val options = listOf(
                "--patch-module", "java.base=${srcDir.absolutePath}",
                "-cp", cp,
                "-d", tempClasses.absolutePath,
            )
            val success = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call()
            if (!success) {
                val errors = diagnostics.diagnostics.joinToString("\n") {
                    "${it.kind}: ${it.getMessage(null)} (${it.source?.name}:${it.lineNumber})"
                }
                throw GradleException("Failed to compile timeCompat Java classes:\n$errors")
            }
        } else {
            execOperations.exec {
                commandLine(
                    "javac",
                    "--patch-module", "java.base=${srcDir.absolutePath}",
                    "-cp", cp,
                    "-d", tempClasses.absolutePath,
                    *javaFiles.map { it.absolutePath }.toTypedArray(),
                )
            }
        }

        val classFiles = project.fileTree(tempClasses) {
            include("**/*.class")
        }.files.map { it.absolutePath }

        val buildToolsDir = File(sdkDirectory.get().asFile, "build-tools/${buildToolsVersion.get()}")
        val isWindows = System.getProperty("os.name").lowercase().contains("windows")
        val d8Exe = File(buildToolsDir, if (isWindows) "d8.bat" else "d8")

        if (d8Exe.exists()) {
            execOperations.exec {
                commandLine(
                    d8Exe.absolutePath,
                    "--min-api", "24",
                    "--output", tempDex.absolutePath,
                    *classFiles.toTypedArray(),
                )
            }
        } else {
            execOperations.javaexec {
                mainClass.set("com.android.tools.r8.D8")
                classpath(File(buildToolsDir, "lib/d8.jar"))
                args("--min-api", "24", "--output", tempDex.absolutePath)
                args(classFiles)
            }
        }

        val generatedDex = File(tempDex, "classes.dex")
        if (generatedDex.exists()) {
            generatedDex.copyTo(File(outDir, "time_compat.dex"), overwrite = true)
        } else {
            throw GradleException("D8 failed to generate classes.dex for time_compat")
        }
    }
}
