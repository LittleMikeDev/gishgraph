package uk.co.littlemike.gishgraph.ddag.git

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GitDDagInitialCommitSpecification extends Specification {

    @Rule
    TemporaryFolder folder
    TestGitDDag myDag
    String myId = "myDag"

    def setup() {
        myDag = new TestGitDDag(folder.root.toPath(), myId)
    }

    def "creates folder for own commits"() {
        expect:
        myDag.workingDirectory.resolve("myDag").toFile().isDirectory()
    }

}
