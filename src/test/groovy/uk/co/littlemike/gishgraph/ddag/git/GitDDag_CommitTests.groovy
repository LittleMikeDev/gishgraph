package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.revwalk.RevCommit

class GitDDag_CommitTests extends GitDDag_2WayTestBase {
    static eventId = "me-2"
    static eventData = utf8(eventId)

    RevCommit myInitialCommit
    RevCommit theirInitialCommit

    def setup() {
        myDag.ddag.createInitialEvent("me-1", utf8("me-1"))
        theirDag.ddag.createInitialEvent("them-1", utf8("them-1"))

        myInitialCommit = myHead()
        theirInitialCommit = theirHead()
    }

    def "Creates file with contents of event"() {
        when:
        iSync(eventId)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(eventId).toFile()
        commitFile.isFile()
        commitFile.bytes == eventData
    }

    def "Commits event"() {
        when:
        def myCommit = iSync(eventId)

        then:
        myDag.localRepo.isClean()
        myCommit != null
        myCommit.fullMessage == "$myId/$eventId"
    }

    def "Commit is a merge commit from my and their initial events"() {
        when:
        def myCommit = iSync(eventId)

        then:
        myCommit.parentCount == 2
        myCommit.parents[0].id == myInitialCommit.id
        myCommit.parents[1].id == theirInitialCommit.id
    }

    def "Commits to my branch"() {
        when:
        def myCommit = iSync(eventId)

        then:
        def branchCommit = myDag.localRepo.findCommit(myId)
        myCommit.id == branchCommit?.id
    }

    def "Pushes merge commit to remote"() {
        when:
        def myCommit = iSync(eventId)

        then:
        def remoteCommit = myDag.remoteRepo.findCommit(myId)
        myCommit.id == remoteCommit?.id
    }

    def "Can commit on top of their event"() {
        given:
        def them2 = theySync("them-2")

        when:
        def me2 = iSync("me-2")

        then:
        me2.parentCount == 2
        me2.parents[0].id == myInitialCommit.id
        me2.parents[1].id == them2.id
    }

    def "Can fetch and build events off each other asynchronously"() {
        given:
        // Simultaneous crossover commits
        def me2 = iSync("me-2")
        theySync("them-2")
        //They catch up
        def them3 = theySync("them-3")
        // I catch up
        def me3 = iSync("me-3")

        expect:
        me3.parents[0].id == me2.id
        me3.parents[1].id == them3.id
    }
}
