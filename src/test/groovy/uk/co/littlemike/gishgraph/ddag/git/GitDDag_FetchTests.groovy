package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_FetchTests extends GitDDag_TestBase {
    static String theirCommitId = "their-commit"
    static byte[] theirCommitData = "Foo & Bar".getBytes("UTF-8")

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
}
