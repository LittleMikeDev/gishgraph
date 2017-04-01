package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.revwalk.RevCommit

class GitDDag_CommitTests extends GitDDag_TestBase {
    static commitId = "me-2"
    static commitData = utf8(commitId)

    RevCommit myInitialCommit
    RevCommit theirInitialCommit

    def setup() {
        myDag.ddag.createInitialCommit("me-1", utf8("me-1"))
        theirDag.ddag.createInitialCommit("them-1", utf8("them-1"))
        iFetch()
        theyFetch()

        myInitialCommit = myHead()
        theirInitialCommit = theirHead()
    }

    def "Creates file with contents of commit"() {
        when:
        iCommit(commitId)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(commitId).toFile()
        commitFile.isFile()
        commitFile.bytes == commitData
    }

    def "Commits commit"() {
        when:
        def myCommit = iCommit(commitId)

        then:
        myDag.localRepo.isClean()
        myCommit != null
        myCommit.fullMessage == commitId
    }

    def "Commit is a merge commit from my and their initial commits"() {
        when:
        def myCommit = iCommit(commitId)

        then:
        myCommit.parentCount == 2
        myCommit.parents[0].id == myInitialCommit.id
        myCommit.parents[1].id == theirInitialCommit.id
    }

    def "Commits to my branch"() {
        when:
        def myCommit = iCommit(commitId)

        then:
        def branchCommit = myDag.localRepo.findCommit(myId)
        myCommit.id == branchCommit?.id
    }

    def "Pushes merge commit to remote"() {
        when:
        def myCommit = iCommit(commitId)

        then:
        def remoteCommit = myDag.remoteRepo.findCommit(myId)
        myCommit.id == remoteCommit?.id
    }

    def "Can commit on top of their commit"() {
        given:
        def them2 = theyCommit("them-2")
        iFetch()

        when:
        def me2 = iCommit("me-2")

        then:
        me2.parentCount == 2
        me2.parents[0].id == myInitialCommit.id
        me2.parents[1].id == them2.id
    }

    def "Can fetch and build commits off each other asynchronously"() {
        given:
        // Simultaneous crossover commits
        def me2 = iCommit("me-2")
        theyCommit("them-2")
        //They catch up
        theyFetch()
        def them3 = theyCommit("them-3")
        // I catch up
        iFetch()
        def me3 = iCommit("me-3")

        expect:
        me3.parents[0].id == me2.id
        me3.parents[1].id == them3.id
    }
}
