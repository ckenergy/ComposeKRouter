package com.ckenergy.compose.kroute

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
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
            val taskProviderTransformAllClassesTask =
                project.tasks.register(
                    "${variant.name}TransformAllClassesTask",
                    TransformAllClassesTask::class.java
                )
            // https://github.com/android/gradle-recipes
            variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                .use(taskProviderTransformAllClassesTask)
                .toTransform(
                    ScopedArtifact.CLASSES,
                    TransformAllClassesTask::allJars,
                    TransformAllClassesTask::allDirectories,
                    TransformAllClassesTask::output
                )
        }

    }

}