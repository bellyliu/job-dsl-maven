job('GITHUB-ansible-job-dsl-CHILDERN'){
    description('This job was built using code (dsl job)')

    parameters {
        choiceParam('AGE',['20', '21', '22', '23', '24', '25'] )
    }

    wrappers {
        colorizeOutput(colorMap = 'xterm')
    }

    steps {
      ansiblePlaybook('/var/jenkins_home/ansible/people.yml'){
            inventoryPath('/var/jenkins_home/ansible/hosts')
            unbufferedOutput(true)
            colorizedOutput(true)
       		extraVars{
              extraVar("PEOPLE_AGE", '${AGE}', false)
                
                 }
      }
    }
}

job('GIT-maven-dsl-job'){

    description('this is maven job using dsl pluggin')

    scm{
        git('https://github.com/bellyliu/simple-java-maven-app.git', 'master')
    }

    steps{

        maven{
            mavenInstallation('jenkins-maven')
         	goals('-B -DskipTests clean package')
    		}
      	maven{
         	mavenInstallation('jenkins-maven')
          	goals('test')
        }

        shell('''
                echo "============="
                echo "DEPLOYING JAR"
                echo "============="
                java -jar /var/jenkins_home/workspace/maven-dsl-job/target/my-app-1.0-SNAPSHOT.jar
                
                ''')
        
    publishers{

        archiveArtifacts {
            pattern('target/*.jar')
        }

        archiveJunit('target/surefire-reports/TEST-com.mycompany.app.AppTest.xml'){
            allowEmptyResults(true)
            healthScaleFactor(1)
        }

        mailer('dekribellyliu@navitaorigo.co.id', true, true)

    }
            

    }
 }