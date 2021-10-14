package com.hexaware.service
class Dockerdeploy implements Serializable{
Script mainScript
Map specs
Map config

  def Dockerdeploy(Script mainScript, Map specs, Map config){
  this.mainScript = mainScript
  this.specs = specs
  this.config = config
  }
  def dockerdeployFunc(Map specs, Map config){
     
      mainScript.sh """ docker run -d -p 8085:8085 --name petclinic jenkinstestpipe.jfrog.io/ui/native/default-docker-local/petclinic:V1 """
    
  }
}
