package uk.co.littlemike.gishgraph.ddag.git

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class GitDDag_InitialisationTests extends Specification {

    @Rule
    TemporaryFolder folder
    TestGitDDag myDag
    String myId = "myDag"

    def setup() {
        myDag = new TestGitDDag(folder.root.toPath(), myId)
    }

    def "initialises git in working directory"() {
        expect:
        myDag.workingDirectory.resolve(".git").toFile().isDirectory()
        myDag.localRepo.isInitialized()
    }

    def "creates folder for own commits"() {
        expect:
        myDag.workingDirectory.resolve("myDag").toFile().isDirectory()
    }

    def "creates file with contents of initial commit"() {
        given:
        def id = "a-commit"
        def data = "Hello world!".getBytes(StandardCharsets.UTF_8)

        when:
        myDag.ddag.createInitialCommit(id, data)

        then:
        def commitFile = myDag.workingDirectory.resolve(myId).resolve(id).toFile()
        commitFile.isFile()
        new String(commitFile.bytes, StandardCharsets.UTF_8) == "Hello world!"
    }

}
