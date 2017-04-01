package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_InitialisationTests extends GitDDag_TestBase {
    static String commitId = "a-commit"
    static byte[] commitData = "Hello world!".getBytes("UTF-8")

    def "initialises git in working directory"() {
        expect:
        myDag.workingDirectory.resolve(".git").toFile().isDirectory()
        myDag.localRepo.isClean()
    }

    def "creates folder for own commits"() {
        expect:
        myDag.workingDirectory.resolve(myId).toFile().isDirectory()
    }

    def "has a branch named after own id instead of master"() {
        expect:
        !myDag.localRepo.refExists("master")
        myDag.localRepo.refExists(myId)
    }

    def "creates file with contents of initial commit"() {
        when:
        myDag.ddag.createInitialCommit(commitId, commitData)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(commitId).toFile()
        commitFile.isFile()
        commitFile.bytes == commitData
    }

    def "commits initial commit"() {
        when:
        myDag.ddag.createInitialCommit(commitId, commitData)

        then:
        myDag.localRepo.isClean()
        def headCommit = myDag.localRepo.findCommit("HEAD")
        headCommit != null
        headCommit.fullMessage == commitId
    }

    def "commits to own branch"() {
        when:
        myDag.ddag.createInitialCommit(commitId, commitData)

        then:
        def headCommit = myDag.localRepo.findCommit("HEAD")
        def branchCommit = myDag.localRepo.findCommit(myId)
        headCommit.id == branchCommit?.id
    }

    def "pushes commit to own remote"() {
        when:
        myDag.ddag.createInitialCommit(commitId, commitData)

        then:
        def localMaster = myDag.localRepo.findCommit(myId)
        def remoteMaster = myDag.remoteRepo.findCommit(myId)
        localMaster.id == remoteMaster?.id
    }
}
