package uk.co.littlemike.gishgraph.ddag.git

import groovy.json.internal.Charsets

class GitDDag_FetchTests extends GitDDag_TestBase {
    static String theirCommitId = "their-commit"
    static byte[] theirCommitData = "Foo & Bar".getBytes(Charsets.UTF_8)

    def "Fetches commits from other repository"() {
        given:
        theirDag.ddag.createInitialCommit(theirCommitId, theirCommitData)

        when:
        myDag.ddag.fetch(theirDag.asRemote())

        then:
        def theirCommit = theirDag.localRepo.findCommit(theirId)
        def fetchedCommit = myDag.localRepo.findCommit("$theirId/$theirId")
        theirCommit.id == fetchedCommit?.id
    }

    def "Can fetch twice from the same repository without error"() {
        given:
        theirDag.ddag.createInitialCommit(theirCommitId, theirCommitData)
        myDag.ddag.fetch(theirDag.asRemote())

        when:
        myDag.ddag.fetch(theirDag.asRemote())

        then:
        def theirCommit = theirDag.localRepo.findCommit(theirId)
        def fetchedCommit = myDag.localRepo.findCommit("$theirId/$theirId")
        theirCommit.id == fetchedCommit?.id
    }
}
