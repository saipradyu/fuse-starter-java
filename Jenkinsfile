pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'chmod u+x ./gradlew' // set file permissions on the wrapper that we just checked out

        // I'm going to call the gradle wrapper rather than use the jenkins gradle plugin
        // https://stackoverflow.com/questions/27064631/jenkins-gradle-integration-invoke-gradle-vs-use-gradle-wrapper-options
        // bigguy (a principal engineer at Gradle, Inc) says to, so clearly we should.
        sh './gradlew build -x test -x check -i'
      }
    }
    stage('SonarQube analysis') {
      steps {
        withSonarQubeEnv('SonarCloud FUSE') {
          sh './gradlew --info sonarqube'
        }
      }
    }
    stage('Quality gate') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          script {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
              error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
          }
        }
      }
    }
    stage('Checkstyle') {
      steps {
        sh './gradlew check -x test -i'
      }
      post {
        failure {
          publishHTML (target: [
            allowMissing: false,
            alwaysLinkToLastBuild: false,
            keepAll: true,
            reportDir: 'build',
            reportFiles: 'checkstyle-result.xml',
            reportName: 'Checkstyle report'
          ])
        }
      }
    }
    stage('Deploy') {
      when {
        not {
          anyOf {
            // sure there's a nicer way of doing this with a regex...

            // commenting out this check briefly while I test the gradle build.
            // expression { BRANCH_NAME.startsWith('feature/') }
            expression { BRANCH_NAME.startsWith('hotfix/') }
            expression { BRANCH_NAME.startsWith('bugfix/') }
          }
        }
      }
      steps {
        // assumes the first Build stage produced the jar in the location referenced by manifest-dev.yml
        pushToCloudFoundry(
          target: 'https://api.run.pivotal.io/',
          pluginTimeout: 180,
          organization: 'FUSE',
          cloudSpace: 'development',
          credentialsId: 'cf-credentials',
          manifestChoice: [manifestFile: 'manifest-dev.yml']
        )
      }
    }
  }
}
