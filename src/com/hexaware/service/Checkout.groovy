package com.hexaware.service
class Checkout implements Serializable{
Script mainScript
Map specs

  def Checkout(Script mainScript, Map specs){
  this.mainScript = mainScript
  this.specs = specs
  }
  def checkOutFunc(Map specs=[]){
    checkout([$class: 'GitSCM',
    branches: [[name: specs.branch]],
    extensions: [],
    userRemoteConfigs: [[url: specs.repo ]]])
  }
}
