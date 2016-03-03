// Folders
def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"

// Variables
def hostProjectRepoUrl = "ssh://jenkins@gerrit:29418/${PROJECT_NAME}/MobileApplication"

// Jobs
def deploy = freeStyleJob(projectFolderName + "/Deployment_To_UAT")

deploy.with{
  description("This job Deploys from git to UAT.")
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
            		passive
			prompt
			
		         lcd /var/jenkins_home/jobs/Hostcentric/jobs/Mainframe/jobs/Deployment_To_Production/workspace/COPYLIB
		         cd 'PDCLIB.UAT.BASELINE.COPYLIB'
		         mput * 
		            
		         lcd /var/jenkins_home/jobs/Hostcentric/jobs/Mainframe/jobs/Deployment_To_Production/workspace/DBRMLIB
		         cd 'PDCLIB.UAT.BASELINE.DBRMLIB'
		         mput *
		            
		         lcd /var/jenkins_home/jobs/Hostcentric/jobs/Mainframe/jobs/Deployment_To_Production/workspace/DCLGEN
		         cd 'PDCLIB.UAT.BASELINE.DCLGEN'
		         mput *
		            
		         lcd /var/jenkins_home/jobs/Hostcentric/jobs/Mainframe/jobs/Deployment_To_Production/workspace/SRCELIB
		         cd 'PDCLIB.UAT.BASELINE.SRCELIB'
		         mput *
		         
			quit
			END_SCRIPT
			exit 0
			
			''')
  } 
}
