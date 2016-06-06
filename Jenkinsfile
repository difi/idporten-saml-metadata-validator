stage 'Dev'
node {
    checkout scm
    sh 'mvn -T 1C clean -Pall'
    sh 'mvn -T 1C compile -Pall'
    sh 'mvn -T 1C test -Pall'
}