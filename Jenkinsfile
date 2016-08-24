stage 'Dev'
node {
    checkout scm
    if (env.BRANCH_NAME == 'develop') {
        sh 'mvn -T 1C clean'
        sh 'mvn -T 1C versions:set'
        sh 'mvn -T 1C package'
        sh 'mvn docker:build'
        sh 'mvn docker:push'
    }
    else {
        sh 'mvn -T 1C clean -Pall'
        sh 'mvn -T 1C compile -Pall'
        sh 'mvn -T 1C test -Pall'
    }
}