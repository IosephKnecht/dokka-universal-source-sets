plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.android.gradlePlugin.impl)
    implementation(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("dokka-convention-plugin") {
            id = "io.github.iosephknecht.dokka.plugin"
            implementationClass = "io.github.iosephknecht.dokka.plugin.DokkaConventionPlugin"
        }
    }
}