plugins {
}

val junitVersion: String? by extra
val hamkrestVersion: String? by extra
val mockkVersion: String? by extra

dependencies {
    implementation(project(":library"))
    implementation(project(":coursetorrent-app"))
	testImplementation("il.ac.technion.cs.softwaredesign:primitive-storage-layer:1.0.1")
	
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junitVersion)
    testImplementation("com.natpryce", "hamkrest", hamkrestVersion)

    testImplementation("io.mockk", "mockk", mockkVersion)
}

/*

Edit this part if you want to log test results

tasks.test{
    useJUnitPlatform()

	 minHeapSize = "256m"
    maxHeapSize = "4g"
    
    // Make sure tests don't take over 10 minutes
    timeout.set(Duration.ofMinutes(10))
    reports{
        junitXml.isEnabled = false
        html.isEnabled = true

        html.destination = File("PATH\\TO\\HTML")
    } 

    testLogging{
        val csvFile=File("PATH\\TO\\CSV")
        var toprow ="ClassName,TestName,Result,Duration(ms)\n"


        var content = ""
        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {
                if(suite.parent == null){
                    csvFile.appendText(toprow)
                }
            }
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                content += testDescriptor.getClassName()+","+
                        testDescriptor.getName()+","+
                        result.resultType.toString()+","+
                        (result.endTime-result.startTime).toString()+"\n"

            }
            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                if(suite.parent == null){
                    println("Logging to csv at "+csvFile.absolutePath)
                    csvFile.appendText(content)
                    content=""
                }

            }
        })
    }

}
*/