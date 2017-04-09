package uk.co.littlemike.gishgraph.ddag.git

import groovy.json.internal.Charsets

class GitDDag_InitialisationTests extends GitDDag_2WayTestBase {
    static String eventId = "an-event"
    static byte[] eventData = "Hello world!".getBytes(Charsets.UTF_8)

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

    def "creates file with contents of initial event"() {
        when:
        myDag.ddag.createInitialEvent(eventId, eventData)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(eventId).toFile()
        commitFile.isFile()
        commitFile.bytes == eventData
    }

    def "commits initial event"() {
        when:
        myDag.ddag.createInitialEvent(eventId, eventData)

        then:
        myDag.localRepo.isClean()
        def commit = myHead()
        commit != null
        commit.fullMessage == eventId
    }

    def "commits to own branch"() {
        when:
        myDag.ddag.createInitialEvent(eventId, eventData)

        then:
        def commit = myHead()
        def branchCommit = myDag.localRepo.findCommit(myId)
        commit.id == branchCommit?.id
    }

    def "pushes commit to own remote"() {
        when:
        myDag.ddag.createInitialEvent(eventId, eventData)

        then:
        def localMaster = myDag.localRepo.findCommit(myId)
        def remoteMaster = myDag.remoteRepo.findCommit(myId)
        localMaster.id == remoteMaster?.id
    }
}
