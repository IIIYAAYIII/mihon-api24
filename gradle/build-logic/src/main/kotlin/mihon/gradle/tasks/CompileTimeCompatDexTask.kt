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

        val javaFiles = srcDir.walkTopDown().filter { it.isFile && it.extension == "java" }.toList()

        if (javaFiles.isEmpty()) return

        if (compileClasspath.files.isEmpty()) {
            throw GradleException("compileClasspath for timeCompat is empty! Desugared library was not resolved.")
        }

        val cp = (compileClasspath.files + bootClasspath.files).joinToString(File.pathSeparator) { it.absolutePath }

        val compiler = ToolProvider.getSystemJavaCompiler()
        if (compiler != null) {
            val diagnostics = DiagnosticCollector<JavaFileObject>()
            val fileManager = compiler.getStandardFileManager(diagnostics, null, null)
            try {
                val compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles)
                val options = listOf(
                    "--patch-module",
                    "java.base=${srcDir.absolutePath}",
                    "--add-reads",
                    "java.base=ALL-UNNAMED",
                    "-cp",
                    cp,
                    "-d",
                    tempClasses.absolutePath,
                )
                val success = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call()
                if (!success) {
                    val errors = diagnostics.diagnostics.joinToString("\n") {
                        "${it.kind}: ${it.getMessage(null)} (${it.source?.name}:${it.lineNumber})"
                    }
                    throw GradleException("Failed to compile timeCompat Java classes:\n$errors")
                }
            } finally {
                fileManager.close()
            }
        } else {
            execOperations.exec {
                standardInput = java.io.InputStream.nullInputStream()
                commandLine(
                    "javac",
                    "--patch-module",
                    "java.base=${srcDir.absolutePath}",
                    "--add-reads",
                    "java.base=ALL-UNNAMED",
                    "-cp",
                    cp,
                    "-d",
                    tempClasses.absolutePath,
                    *javaFiles.map { it.absolutePath }.toTypedArray(),
                )
            }
        }

        val classFiles = tempClasses.walkTopDown().filter { it.isFile && it.extension == "class" }.toList()
        if (classFiles.isEmpty()) {
            throw GradleException("No class files produced for timeCompat")
        }

        val buildToolsParent = File(sdkDirectory.get().asFile, "build-tools")
        val requestedVersion = buildToolsVersion.orNull
        val buildToolsDir = if (!requestedVersion.isNullOrBlank() &&
            File(buildToolsParent, requestedVersion).exists()
        ) {
            File(buildToolsParent, requestedVersion)
        } else {
            buildToolsParent.listFiles()?.filter { it.isDirectory }?.maxByOrNull { it.name }
                ?: File(buildToolsParent, requestedVersion ?: "35.0.0")
        }

        val d8Jar = File(buildToolsDir, "lib/d8.jar").takeIf { it.exists() }
            ?: File(buildToolsDir, "d8.jar").takeIf { it.exists() }
            ?: throw GradleException("Could not find d8.jar in ${buildToolsDir.absolutePath}")

        val d8Args = mutableListOf(
            "--min-api",
            "24",
            "--output",
            tempDex.absolutePath,
        )
        for (f in bootClasspath.files) {
            d8Args.add("--lib")
            d8Args.add(f.absolutePath)
        }
        for (f in compileClasspath.files) {
            d8Args.add("--classpath")
            d8Args.add(f.absolutePath)
        }
        d8Args.addAll(classFiles.map { it.absolutePath })

        execOperations.javaexec {
            mainClass.set("com.android.tools.r8.D8")
            classpath(d8Jar)
            args(d8Args)
        }

        val generatedDex = File(tempDex, "classes.dex")
        if (generatedDex.exists()) {
            generatedDex.copyTo(File(outDir, "time_compat.dex"), overwrite = true)
        } else {
            throw GradleException("D8 failed to generate classes.dex for time_compat")
        }
    }
}
