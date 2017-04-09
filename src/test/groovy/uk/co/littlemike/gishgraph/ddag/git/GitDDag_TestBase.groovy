package uk.co.littlemike.gishgraph.ddag.git

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GitDDag_TestBase extends Specification {

    @Rule
    TemporaryFolder folder

    TestGitDDag testGitDDag(String id) {
        new TestGitDDag(folder.root.toPath().resolve(id), id)
    }
}
