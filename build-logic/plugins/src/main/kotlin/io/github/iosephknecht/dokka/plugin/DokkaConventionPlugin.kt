package io.github.iosephknecht.dokka.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec

internal class DokkaConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target
            .extensions
            .extraProperties
            .set(
                "org.jetbrains.dokka.experimental.gradle.pluginMode",
                "V2Enabled"
            )

        val conventionMode = target
            .property("io.github.iosephknecht.dokka.plugin.convention.mode")
            .let { requireNotNull(it) { "mode property must not be null" } }
            .run { this as String }
            .let { DokkaConventionMode.valueOf(it) }

        println("plugin start with mode $conventionMode")

        target.pluginManager.apply(DokkaPlugin::class.java)

        when (conventionMode) {
            DokkaConventionMode.DEFAULT -> withoutSuppressingSourceSets(target)
            DokkaConventionMode.WITH_SUPPRESSED_SOURCE_SETS -> suppressSourceSets(target)
            DokkaConventionMode.WITH_WORKAROUND_SUPPRESSED_SOURCE_SETS -> suppressSourceSetsWithWorkaround(target)
        }
    }

    private companion object {

        fun withoutSuppressingSourceSets(target: Project) {
            val extension = target
                .extensions
                .getByType(DokkaExtension::class.java)

            target
                .pluginManager
                .withPlugin("com.android.base") {
                    extension.dokkaSourceSets.configureEach { sourceSet ->
                        sourceSet.logEnabled()
                    }
                }
        }

        fun suppressSourceSets(target: Project) {
            val extension = target
                .extensions
                .getByType(DokkaExtension::class.java)

            target
                .pluginManager
                .withPlugin("com.android.base") {
                    extension.dokkaSourceSets.configureEach { sourceSet ->
                        val isEnabled = sourceSet.isEnabled
                        sourceSet.suppress.set(!isEnabled)
                        sourceSet.logEnabled()
                    }
                }
        }

        fun suppressSourceSetsWithWorkaround(target: Project) {
            val extension = target
                .extensions
                .getByType(DokkaExtension::class.java)

            target
                .pluginManager
                .withPlugin("com.android.base") {
                    val androidExt = target.extensions.getByType(BaseExtension::class.java)

                    val variants = when (androidExt) {
                        is LibraryExtension -> androidExt.libraryVariants
                        is AppExtension -> androidExt.applicationVariants
                        is TestExtension -> androidExt.applicationVariants
                        else -> error("unsupported android gradle plugin type")
                    }

                    extension.dokkaSourceSets.configureEach { sourceSet ->
                        val isEnabled = sourceSet.isEnabled
                        sourceSet.suppress.set(!isEnabled)
                        sourceSet.logEnabled()

                        if (!isEnabled) return@configureEach

                        val workaroundClasspath = target.objects.fileCollection()

                        workaroundClasspath.from(androidExt.bootClasspath)

                        variants.all { variant ->
                            if (variant.name != sourceSet.name) return@all

                            workaroundClasspath.from(
                                variant
                                    .compileConfiguration
                                    .incoming
                                    .artifactView {
                                        it.attributes.attribute(
                                            ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE,
                                            ArtifactTypeDefinition.JAR_TYPE
                                        )
                                    }
                                    .files
                            )
                        }

                        sourceSet.classpath.setFrom(workaroundClasspath)
                    }
                }
        }

        val DokkaSourceSetSpec.isEnabled
            get() = name == "release" || name == "main"

        fun DokkaSourceSetSpec.logEnabled() {
            println("Dokka source set: $name ${if (!suppress.get()) "enabled" else "disabled"}")
        }
    }
}