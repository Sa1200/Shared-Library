def checkoutVarFunc(Map specs, Script mainScript) {
    mainScript.checkout([$class: 'GitSCM',
    branches: [[name: specs.branch]],
    extensions: [],
    userRemoteConfigs: [[url: specs.repo ]]
    ])
  }
def build(Map specs, Map config) {
  dobuild = new com.hexaware.service.Build(this, specs, config)
  dobuild.buildFunc(specs, config) 
}
def unittest(Map specs, Map config) {
  dounittest = new com.hexaware.service.Unittesting(this, specs, config)
  dounittest.unitTestFunc(specs, config) 
}
def codecoverage(Map specs, Map config) {
  docodecoverage = new com.hexaware.service.Codecoverage(this, specs, config)
  docodecoverage.codecoverageCheckFunc(specs, config) 
}
def codequality(Map specs, Map config) {
  docodequality = new com.hexaware.service.Codequality(this, specs, config)
  docodequality.codequalityFunc(specs, config) 
}
def artifactupload(Map specs, Map config) {
  doartifactupload = new com.hexaware.service.Uploadartifacts(this, specs, config)
  doartifactupload.uploadartifactFunc(specs, config) 
}
def dockerbuild(Map specs, Map config) {
  dodockerbuild = new com.hexaware.service.Dockerbuild(this, specs, config)
  dodockerbuild.dockerbuildCheckFunc(specs, config)
}

