plugins {
    `project-library`
}

dependencies {
    api(project(":crt-api"))
    api(project(":crt-common"))
    api(project(":crt-parser-html"))
    implementation("org.commonmark:commonmark:0.19.0")
}
