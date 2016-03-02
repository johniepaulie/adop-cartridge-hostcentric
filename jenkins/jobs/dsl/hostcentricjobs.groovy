// Folders
def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"

// Variables
def hostProjectRepoUrl = "ssh://jenkins@gerrit:29418/${PROJECT_NAME}/MobileApplication"

// Jobs
def deploy = freeStyleJob(projectFolderName + "/Deployment_To_Production")

deploy.with{
  description("This job Deploys from git to Production.")
  environmentVariables {
      env('WORKSPACE_NAME',workspaceFolderName)
      env('PROJECT_NAME',projectFolderName)
  }
  wrappers {
    preBuildCleanup()
    sshAgent("adop-jenkins-master")
  }
  scm{
    git{
      remote{
        url(hostProjectRepoUrl)
        credentials("adop-jenkins-master")
      }
      branch("*/master")
    }
  }
  environmentVariables {
      env('WORKSPACE_NAME',workspaceFolderName)
      env('PROJECT_NAME',projectFolderName)
  }
  steps {
	shell ('''#!/bin/sh
			HOST='192.86.33.23'
			USER='mdc012'
			PASSWD='leahcim0'
		
			ftp -n $HOST <<END_SCRIPT
			quote USER $USER
			quote PASS $PASSWD
			prompt
			cd MY.ASM.DEVOPS
			quit
			END_SCRIPT
			exit 0
			''')
  } 
}
