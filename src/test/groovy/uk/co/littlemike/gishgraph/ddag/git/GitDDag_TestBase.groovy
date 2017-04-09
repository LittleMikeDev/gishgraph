package uk.co.littlemike.gishgraph.ddag.git

import kotlin.text.Charsets
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GitDDag_TestBase extends Specification {
    static String myId = "me"
    static String theirId = "them"

    @Rule
    TemporaryFolder folder
    TestGitDDag myDag
    TestGitDDag theirDag

    def setup() {
        myDag = new TestGitDDag(folder.root.toPath().resolve(myId), myId)
        theirDag = new TestGitDDag(folder.root.toPath().resolve(theirId), theirId)
    }

    def iFetch() {
        myDag.ddag.fetch(theirDag.remote())
    }

    def theyFetch() {
        theirDag.ddag.fetch(myDag.remote())
    }

    def iCommit(String eventId) {
        myDag.ddag.createEvent(eventId, utf8(eventId), theirDag.remote())
        return myHead()
    }

    def theyCommit(String eventId) {
        theirDag.ddag.createEvent(eventId, utf8(eventId), myDag.remote())
        return theirHead()
    }

    def myHead() {
        return myDag.localRepo.findCommit("HEAD")
    }

    def theirHead() {
        return theirDag.localRepo.findCommit("HEAD")
    }

    static utf8(String string) {
        return string.getBytes(Charsets.UTF_8)
    }
}
