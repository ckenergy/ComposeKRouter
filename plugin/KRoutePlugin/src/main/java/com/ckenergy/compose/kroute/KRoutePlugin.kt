package com.ckenergy.compose.kroute

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class KRoutePlugin : Plugin<Project> {

    companion object {
        val graphList = hashSetOf<String>()

    }

    override fun apply(project: Project) {
        val extension = project.extensions.getByType(AndroidComponentsExtension::class.java)

        graphList.clear()

        extension.onVariants(extension.selector().all()) { variant ->
            variant.instrumentation.apply {
                transformClassesWith(CollectClassVisitorFactory::class.java, InstrumentationScope.ALL) {
                }
                transformClassesWith(KRouteASMFactory::class.java, InstrumentationScope.ALL) {
                    it.registerMap.set(graphList)
                }
                setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
            }
        }

    }

}