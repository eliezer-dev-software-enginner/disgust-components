rootProject.name = "disgust-components"
include("disgust-icons")

// The showcase is opt-in: ordinary build/test/publication never configure it.
if (gradle.startParameter.taskNames.any {
        it.startsWith(":disgust-ui:") || it.startsWith("disgust-ui:")
    } || providers.gradleProperty("withUi").orNull == "true") {
    include("disgust-ui")
}
