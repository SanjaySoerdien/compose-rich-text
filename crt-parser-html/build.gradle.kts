plugins {
    `project-library`
}

dependencies {
    api(project(":crt-api"))
    api(project(":crt-common"))
    implementation("org.commonmark:commonmark:0.19.0")
    // add the dependenciy to jsoup
    implementation("org.jsoup:jsoup:1.16.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")

}
