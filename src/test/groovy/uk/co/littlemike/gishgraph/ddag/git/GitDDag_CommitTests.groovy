package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.revwalk.RevCommit

class GitDDag_CommitTests extends GitDDag_TestBase {
    static eventId = "me-2"
    static eventData = utf8(eventId)

    RevCommit myInitialCommit
    RevCommit theirInitialCommit

    def setup() {
        myDag.ddag.createInitialEvent("me-1", utf8("me-1"))
        theirDag.ddag.createInitialEvent("them-1", utf8("them-1"))
        iFetch()
        theyFetch()

        myInitialCommit = myHead()
        theirInitialCommit = theirHead()
    }

    def "Creates file with contents of event"() {
        when:
        iCommit(eventId)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(eventId).toFile()
        commitFile.isFile()
        commitFile.bytes == eventData
    }

    def "Commits event"() {
        when:
        def myCommit = iCommit(eventId)

        then:
        myDag.localRepo.isClean()
        myCommit != null
        myCommit.fullMessage == eventId
    }

    def "Commit is a merge commit from my and their initial events"() {
        when:
        def myCommit = iCommit(eventId)

        then:
        myCommit.parentCount == 2
        myCommit.parents[0].id == myInitialCommit.id
        myCommit.parents[1].id == theirInitialCommit.id
    }

    def "Commits to my branch"() {
        when:
        def myCommit = iCommit(eventId)

        then:
        def branchCommit = myDag.localRepo.findCommit(myId)
        myCommit.id == branchCommit?.id
    }

    def "Pushes merge commit to remote"() {
        when:
        def myCommit = iCommit(eventId)

        then:
        def remoteCommit = myDag.remoteRepo.findCommit(myId)
        myCommit.id == remoteCommit?.id
    }

    def "Can commit on top of their event"() {
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

    def "Can fetch and build events off each other asynchronously"() {
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
