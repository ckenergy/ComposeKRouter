package com.ckenergy.compose.kroute

import com.ckenergy.compose.kroute.utils.InjectUtils
import com.ckenergy.compose.kroute.utils.ScanSetting
import com.ckenergy.compose.kroute.utils.ScanUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class TransformKRouterClassesTask : DefaultTask() {

    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val startTime = System.currentTimeMillis()

        val jarOutput = JarOutputStream(
            BufferedOutputStream(
                FileOutputStream(
                    output.get().asFile
                )
            )
        )

        val targetList: List<ScanSetting> = listOf(
            ScanSetting("INavGraphProvider"),
        )
        var originInject: ByteArray? = null
        allJars.get().forEach { file ->
//            log("handling " + file.asFile.getAbsolutePath())
            val jarFile = JarFile(file.asFile)
            jarFile.entries().iterator().forEach { entry ->
//                log("Adding from jar ${entry.name}")
                try {
                    if (!entry.isDirectory) {
                        if (entry.name != ScanSetting.GENERATE_TO_CLASS_FILE_NAME) {
                            // Scan and choose
                            if (ScanUtils.shouldProcessClass(entry.name)) {
                                jarFile.getInputStream(entry).use { inputs ->
                                    ScanUtils.scanClass(inputs, targetList, false)
                                }
                            }
                            // Copy
                            jarOutput.saveEntry(entry.name, jarFile.getInputStream(entry))
                        } else {
                            // Skip
//                            log("Find inject byte code, Skip ${entry.name}")
                            jarFile.getInputStream(entry).use { inputs ->
                                originInject = inputs.readAllBytes()
                                // println("Find before originInject is ${originInject?.size}")
                            }
                        }
                    }
                } catch (e: Exception) {
//                    log(e.stackTraceToString())
                }
            }
            jarFile.close()
        }
        allDirectories.get().forEach { directory ->
//            log("handling " + directory.asFile.getAbsolutePath())
            directory.asFile.walk().forEach { file ->
                if (file.isFile) {
//                    log("Found $file.name")
                    val relativePath = directory.asFile.toURI().relativize(file.toURI()).getPath()
//                    log("Adding from directory ${relativePath.replace(File.separatorChar, '/')}")
                    if (ScanUtils.shouldProcessClass(file.name)) {
                        file.inputStream().use { input ->
                            ScanUtils.scanClass(input, targetList, false)
                        }
                    }
                    jarOutput.saveEntry(relativePath.replace(File.separatorChar, '/'), file.inputStream())
                }
            }
        }

        log("Start inject byte code")
        if (originInject == null) { // Check
            error("Can not find KRouter inject point, Do you import KRouter?")
        }
        val resultByteArray = InjectUtils.referHackWhenInit(
            ByteArrayInputStream(originInject), targetList
        )
        jarOutput.saveEntry(ScanSetting.GENERATE_TO_CLASS_FILE_NAME, ByteArrayInputStream(resultByteArray))
        log("Inject byte code successful")

        jarOutput.close()

        println("transfom time:${System.currentTimeMillis() - startTime}")

    }

    private fun JarOutputStream.saveEntry(entryName: String, inputStream: InputStream) {
        this.putNextEntry(JarEntry(entryName))
        inputStream.use {
            it.copyTo(this)
        }
        this.closeEntry()
    }


    private fun log(msg: String) {
        println("TransformAllClassesTask==== $msg")
    }
}