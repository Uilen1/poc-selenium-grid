node() {
    env.SERVICE_NAME = "poc-selenium-grid"
    def environment = env.ENVIRONMENT
    def tags = env.FEATURE
    def parallel = env.PARALLEL == 'sim' ? true : false
    def execution = env.PARALLEL == 'sim' ? 'Paralela' : 'Sequencial'


    try {
        def gradle = "./gradlew"
        stage ('Clone'){
            git branch:'master', url: 'seu-repositorio'
        }
        stage('DockerCompose - UP'){
                sh 'docker-compose down' // Para garantir que não haja resíduos de execução anterior
                echo "**** INICIANDO O DOCKER . . .****"
                sh 'docker-compose up -d'
        }
        stage ('Functional Tests') {
            echo "#######################################\n" +
                 "#                                     #\n" +
                 "#       EVIDÊNCIAS DE TESTES          #\n" +
                 "#                                     #\n" +
                 "#######################################"
            echo "**** Testes automatizados iniciados no ambiente de QA =D ****"
            sh "${gradle} clean seleniumTest -Dparallel.execution='${parallel}' -Dcucumber.filter.tags='@${tags} and not @failed' --info"
            echo "#######################################\n" +
                 "#######################################\n" +
                 "#######################################\n" +
                 "#######################################\n"
        }
    }
    catch (Exception exception) {
        throw exception
    }
    finally {
        stage('DockerCompose - DOWN'){
            echo "**** FINALIZANDO O DOCKER . . .****"
            sh 'docker-compose down'
            echo "**** DOCKER FINALIZADO . . .****"
        }
        stage ('Cucumber Report') {
            cucumber failedFeaturesNumber: -1,
                failedScenariosNumber: -1,
                failedStepsNumber: -1,
                fileIncludePattern: '**/cucumber.json',
                pendingStepsNumber: -1,
                skippedStepsNumber: -1,
                sortingMethod: 'ALPHABETICAL',
                undefinedStepsNumber: -1
        }
    }
}