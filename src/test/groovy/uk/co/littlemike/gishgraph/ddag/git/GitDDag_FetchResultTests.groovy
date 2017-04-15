package uk.co.littlemike.gishgraph.ddag.git

class GitDDag_FetchResultTests extends GitDDag_TestBase {

    TestGitDDag alice
    TestGitDDag bob
    TestGitDDag charles

    def setup() {
        alice = testGitDDag("alice")
        bob = testGitDDag("bob")
        charles = testGitDDag("charles")

        alice.createInitialEvent("a-1")
        bob.createInitialEvent("b-1")
        charles.createInitialEvent("c-1")
    }

    def "returns fetch result"() {
        when:
        FetchResult result = alice.fetch(bob)

        then:
        result == new FetchResult([
                new FetchedEvent("b-1", utf8("b-1"))
        ])
    }
}
