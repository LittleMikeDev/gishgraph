package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_2WayTestBase extends GitDDag_TestBase {
    static String myId = "me"
    static String theirId = "them"

    TestGitDDag myDag
    TestGitDDag theirDag

    def setup() {
        myDag = testGitDDag(myId)
        theirDag = testGitDDag(theirId)
    }

    def iSync(String eventId) {
        myDag.ddag.sync(eventId, utf8(eventId), theirDag.remote())
        return myHead()
    }

    def theySync(String eventId) {
        theirDag.ddag.sync(eventId, utf8(eventId), myDag.remote())
        return theirHead()
    }

    def myHead() {
        return myDag.localRepo.findCommit("HEAD")
    }

    def theirHead() {
        return theirDag.localRepo.findCommit("HEAD")
    }
}
