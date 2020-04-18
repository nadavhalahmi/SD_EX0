plugins {
}

val junitVersion: String? by extra
val hamkrestVersion: String? by extra
val mockkVersion: String? by extra

dependencies {
    implementation(project(":library"))
    implementation(project(":coursetorrent-app"))

    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junitVersion)
    testImplementation("com.natpryce", "hamkrest", hamkrestVersion)

    testImplementation("io.mockk", "mockk", mockkVersion)
}