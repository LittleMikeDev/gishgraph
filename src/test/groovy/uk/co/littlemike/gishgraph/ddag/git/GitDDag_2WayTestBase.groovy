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

    def iFetch() {
        myDag.ddag.fetch(theirDag.remote())
    }

    def theyFetch() {
        theirDag.ddag.fetch(myDag.remote())
    }

    def iCreateEvent(String eventId) {
        myDag.ddag.createEvent(eventId, utf8(eventId), theirDag.remote())
        return myHead()
    }

    def theyCreateEvent(String eventId) {
        theirDag.ddag.createEvent(eventId, utf8(eventId), myDag.remote())
        return theirHead()
    }

    def iSync(String eventId) {
        iFetch()
        return iCreateEvent(eventId)
    }

    def theySync(String eventId) {
        theyFetch()
        return theyCreateEvent(eventId)
    }

    def myHead() {
        return myDag.localRepo.findCommit("HEAD")
    }

    def theirHead() {
        return theirDag.localRepo.findCommit("HEAD")
    }
}
