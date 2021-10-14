def call(){
  node('master') {
    def specs = [:]
    try {
        stage('Code Checkout'){
        cleanWs()
        ciFunc.checkoutVarFunc([
        repo: Repo,
        branch: Branch
      ])
      stage('reading GlobalConfig & Specs'){ 
        try {
          println "reading the specs from Specs repository"
          def specsDir = "./specs/$Version"
          println "specs version" + specsDir
            if(fileExists(specsDir + "/commonspecs.yml")){
             commonspecs_template = readYaml file : specsDir + "/commonspecs.yml"
             specs = specs + commonspecs_template
             config = readYaml text: specsDir + "/stagingcommands.yml"
             println "reading specs file" + specs

      
            config_template = readYaml file : specsDir + "/commonspecs.yml"
            config = config_template

            }
        }
        catch(Exception e) {
             println "Error in reading specs file : " + e.getMessage()
        throw e
            }
      }
    }    
    stage('Build'){
      ciFunc.build(specs, config)
      }
      if (specs.unitTest.isUnittestRequired && specs.containsKey("unitTest")){
      stage('UnitTest'){
        ciFunc.unittest(specs, config)
      }
   }
      else {
      println "Skipping unit test stage because unit Test templates are missing or Unit Test stage is disabled."
      }
    if (specs.codeCoverage.isCodecoverageRequired && specs.containsKey("codeCoverage")){  
    stage('CodeCoverage'){
      ciFunc.codecoverage(specs, config)
      }
    } 
    else {
    println "Skipping code coverage stage because code coverage templates are missing or code coverage stage is disabled." 
      }
      
    if (specs.codeQuality.isCodeQualityRequired && specs.containsKey("codeQuality")){  
    stage('CodeQuality'){
      ciFunc.codequality(specs, config)
      }
    } 
    else {
    println "Skipping code quality stage because code quality templates are missing or code quality stage is disabled." 
      }
      
    stage('upload artifact') {
      ciFunc.artifactupload(specs, config)
      }
    if (specs.dockerBuild.isDockerBuildRequired && specs.containsKey("dockerBuild")){  
    stage('DockerBuild'){
      ciFunc.dockerbuild(specs, config)
      }
    } 
    else {
    println "Skipping docker build stage because docker build templates are missing or docker build stage is disabled." 
      }
    if (specs.dockerDeploy.isDockerDeployRequired && specs.containsKey("dockerDeploy")){  
    stage('DockerDeploy'){
      cdFunc.dockerDeploy(specs, config)
      }
    } 
    else {
    println "Skipping docker deploy stage because docker deploy templates are missing or docker deploy stage is disabled." 
      }
    }
    catch(Exception e) {
      println "Error in build stage : " + e.getMessage()
    throw e
      }
    }
  }
