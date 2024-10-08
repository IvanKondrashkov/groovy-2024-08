@Library('jenkins-lib') _

node {
    def listBranch = []
    def baseUrl = 'https://github.com/IvanKondrashkov/groovy-2024-08.git'

    parameters {
        string(name: 'BRANCH', defaultValue: 'main', description: 'Name of branch.')
    }

    stage('Preparation') {
        script {
            git branch: params.BRANCH, url: baseUrl
            listBranch = sh script: 'git ls-remote --heads ${baseUrl}', returnStdout: true
            listBranch = listBranch.readLines().collect {
                it.split()[1].replaceAll("refs/heads/", "")

            }.sort().reverse()

            echo listBranch.toString()
        }
    }

    stage('Initialize docker') {
        def dockerHome = tool 'docker'
        env.PATH = "${dockerHome}/bin:${env.PATH}"

        sh 'docker --version'
    }

    stage('Test') {
        def testBranch = listBranch.collectEntries {
            ["Test branch ${it}":  transformTest(baseUrl, it)]
        }

        parallel testBranch
    }

    stage('Build') {
        def buildBranch = listBranch.collectEntries {
            ["Build branch ${it}":  transformBuild(baseUrl, it)]
        }

        parallel buildBranch
    }

    stage('Build and push docker image') {
        def buildAndPushDockerBranch = listBranch.collectEntries {
            ["Build and push docker image branch ${it}":  transformBuildAndPushDocker(baseUrl, it)]
        }

        parallel buildAndPushDockerBranch
    }

    stage('Count files') {
        def countFilesBranch = listBranch.collectEntries {
            ["Count files branch ${it}":  countFilesModule(it)]
        }

        parallel countFilesBranch
    }
}