package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_FetchTests extends GitDDag_2WayTestBase {
    static String theirEventId = "their-event"
    static byte[] theirEventData = utf8("Foo & Bar")

    def setup() {
        myDag.ddag.createInitialEvent("me-1", utf8("me-1"))
    }

    def "Fetches commits from other repository"() {
        given:
        theirDag.ddag.createInitialEvent(theirEventId, theirEventData)

        when:
        iFetch()

        then:
        def theirCommit = theirHead()
        def fetchedCommit = myDag.localRepo.findCommit("$theirId/$theirId")
        theirCommit.id == fetchedCommit?.id
    }

    def "Can fetch twice from the same repository without error"() {
        given:
        theirDag.ddag.createInitialEvent(theirEventId, theirEventData)
        iFetch()

        when:
        iFetch()

        then:
        def theirCommit = theirHead()
        def fetchedCommit = myDag.localRepo.findCommit("$theirId/$theirId")
        theirCommit.id == fetchedCommit?.id
    }
}
