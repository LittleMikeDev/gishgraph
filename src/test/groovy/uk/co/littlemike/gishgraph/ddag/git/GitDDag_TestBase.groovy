package uk.co.littlemike.gishgraph.ddag.git

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
}
