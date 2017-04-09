package uk.co.littlemike.gishgraph.ddag.git

import kotlin.text.Charsets
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GitDDag_TestBase extends Specification {

    @Rule
    TemporaryFolder folder

    TestGitDDag testGitDDag(String id) {
        new TestGitDDag(folder.root.toPath().resolve(id), id)
    }

    static utf8(String string) {
        return string.getBytes(Charsets.UTF_8)
    }
}
