plugins {
    application
}

application {
    mainClassName = "il.ac.technion.cs.softwaredesign.MainKt"
}

val junitVersion: String? by extra
val hamkrestVersion: String? by extra
val mockkVersion: String? by extra

dependencies {
    implementation(project(":library"))
    //implementation(project(":coursetorrent-app"))
    implementation("il.ac.technion.cs.softwaredesign:primitive-storage-layer:1.0.1")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junitVersion)
    testImplementation("com.natpryce", "hamkrest", hamkrestVersion)
    testImplementation("io.mockk", "mockk", mockkVersion)
}