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
            if(fileExists(specsDir + "/commonspecs.yaml")){
             commonspecs_template = readYaml file : specsDir + "/commonspecs.yaml"
             specs = specs + commonspecs_template
        
             println "reading specs file" + specs

            }
          
          config_template = readYaml file : specsDir + "/stagingcommands.yaml"
          config = config_template
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
    
    if (specs.unitTest.isUnittestRequired){
      stage('UnitTest'){
        ciFunc.unittest(specs, config)
      }
    }
    else {
      println "Unit Test stage is disabled."
      }

    if (specs.codeCoverage.isCodecoverageRequired){  
    stage('CodeCoverage'){
      ciFunc.codecoverage(specs, config)
      }
    } 
    else {
    println "code coverage stage is disabled." 
    }
      
    if (specs.codeQuality.isCodeQualityRequired){  
    stage('CodeQuality'){
      ciFunc.codequality(specs, config)
      }
    } 
    else {
    println "code quality stage is disabled." 
      }
      
    stage('upload artifact') {
      ciFunc.artifactupload(specs, config)
      }

    if (specs.dockerBuild.isDockerBuildRequired){  
    stage('DockerBuild'){
      ciFunc.dockerbuild(specs, config)
      }
    } 
    else {
    println "docker build stage is disabled." 
      }
    if (specs.dockerDeploy.isDockerDeployRequired){  
    stage('DockerDeploy'){
      cdFunc.dockerDeploy(specs, config)
      }
    } 
    else {
    println "docker deploy stage is disabled." 
      }
    }
    catch(Exception e) {
      println "Error in build stage : " + e.getMessage()
    throw e
      }
    }
  }
