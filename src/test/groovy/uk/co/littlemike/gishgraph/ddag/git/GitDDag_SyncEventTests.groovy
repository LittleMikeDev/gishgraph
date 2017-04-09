package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_SyncEventTests extends GitDDag_TestBase {

    def alice
    def bob
    def charles

    def setup() {
        alice = testGitDDag("alice")
        bob = testGitDDag("bob")
        charles = testGitDDag("charles")

        alice.createInitialEvent("a-1")
        bob.createInitialEvent("b-1")
        charles.createInitialEvent("c-1")
    }

    def "returns sync result"() {
        when:
        SyncResult syncResult = alice.sync("a-2", bob)

        then:
        syncResult
    }
}
