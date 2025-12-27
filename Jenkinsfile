pipeline{
	agent any
	triggers {
        pollSCM('H/5 * * * *')
    }
	options{
		buildDiscarder(logRotator(numToKeepStr: "5"))
	}
	environment{
		Name = "url-shortner"
		Tag = "v1.0.${BUILD_NUMBER}"
		Image = "sandeepkrjsr/url-shortner"
	}
	parameters{
		booleanParam(
			name: 'BUILD_DOCKER_IMAGE', 
			defaultValue: false, 
			description: 'Build docker image'
		)
		booleanParam(
			name: 'PUSH_DOCKER_IMAGE', 
			defaultValue: false, 
			description: 'Push docker image to registry'
		)
		booleanParam(
			name: 'DEPLOY_CONTAINER', 
			defaultValue: false, 
			description: 'Deploy container after build'
		)
	}
	stages{
		stage("Checkout"){
			steps{
				git url: "https://github.com/sandeepkrjsr/url-shortner.git", branch: "master"
			}
		}
		stage("Build JAR"){
			steps{
				sh "mvn clean package -DskipTests"
			}
		}
		stage("Build Artifact"){
			steps{
				sh "cp target/*.jar target/${Name}-${Tag}.jar"
				archiveArtifacts artifacts: "target/${Name}-${Tag}.jar", fingerprint: true
			}
		}
		stage('Build Docker Image'){
			when{
                expression { params.BUILD_DOCKER_IMAGE }
            }
			steps{
				sh "docker build -t ${Name}:${Tag} ."
			}
		}
		stage('Push Docker Image'){
			when{
                expression { params.PUSH_DOCKER_IMAGE }
            }
			steps{
				withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                    sh "echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin"
                }
				sh "docker tag ${Name}:${Tag} ${Image}:${Tag}"
				sh "docker push ${Image}:${Tag}"
			}
		}
		stage('Container Deployment'){
			when{
                expression { params.DEPLOY_CONTAINER }
            }
			steps{
				sh "docker-compose up -d --pull always"
			}
		}
	}
	post{
		always{
			cleanWs()
		}
		success{
			echo "Build and Deployed Successfully: $Name:$Tag"
		}
		failure{
			echo "Build failed. Please check the logs."
		}
	}
}