import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    `project-android-application`
}

android {
    namespace = "nl.jjkester.crt.demo"

    defaultConfig {
        applicationId = "nl.jjkester.crt.demo"
        versionCode = 1
        versionName = "${project.version}"
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
        )
    }
}

dependencies {
    // App UI
    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation(libs.compose.ui.toolingPreview)
    debugImplementation(libs.compose.ui.tooling)
    implementation("androidx.navigation:navigation-compose")

    // Project dependencies
    implementation(project(":crt-api"))
    implementation(project(":crt-common"))
    implementation(project(":crt-parser-markdown"))
    implementation(project(":crt-renderer-compose"))

    implementation(project(":utils:slf4j-android"))
}

// Copy project README to resources to use in the demo app
tasks.register<Copy>("copyProjectReadmeToRawRes") {
    val sourceDir = rootProject.layout.projectDirectory
    val sourceFileName = "README.md"
    val targetDir = layout.projectDirectory.dir("src/main/res/raw")
    val targetFileName = "main_readme.md"

    val inputFile = sourceDir.file(sourceFileName)
    val outputFile = targetDir.file(targetFileName)

    from(inputFile)
    into(targetDir)

    inputs.file(inputFile)
    outputs.file(outputFile)
}.also { tasks.preBuild.dependsOn(it) }