package com.ckenergy.compose.kroute

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project

class KRoutePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.getByType(AndroidComponentsExtension::class.java)

        extension.onVariants(extension.selector().all()) { variant ->
            val taskProviderTransformKRouterClassesTask =
                project.tasks.register(
                    "${variant.name}TransformKRouterClassesTask",
                    TransformKRouterClassesTask::class.java
                )
            // https://github.com/android/gradle-recipes
            variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                .use(taskProviderTransformKRouterClassesTask)
                .toTransform(
                    ScopedArtifact.CLASSES,
                    TransformKRouterClassesTask::allJars,
                    TransformKRouterClassesTask::allDirectories,
                    TransformKRouterClassesTask::output
                )
        }

    }

}