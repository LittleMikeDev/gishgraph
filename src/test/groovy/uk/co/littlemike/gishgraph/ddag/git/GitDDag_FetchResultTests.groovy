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

    def "returns same fetch result if no commit was made"() {
        given:
        FetchResult originalResult = alice.fetch(bob)

        when:
        FetchResult secondFetchResult = alice.fetch(bob)

        then:
        secondFetchResult == originalResult
    }

    def "may return multiple events"() {
        given:
        bob.sync("b-2", charles)

        when:
        FetchResult result = alice.fetch(bob)

        then:
        result.events.last() == new FetchedEvent("b-2", utf8("b-2"))
        result.events.containsAll(
                new FetchedEvent("b-1", utf8("b-1")),
                new FetchedEvent("c-1", utf8("c-1"))
        )
    }

    def "does not return events previously fetched after commit"() {
        given:
        alice.sync("a-2", bob)
        bob.sync("b-2", alice)

        when:
        FetchResult result = alice.fetch(bob)

        then:
        result.events == [
                new FetchedEvent("b-2", utf8("b-2"))
        ]
    }

    def "does not return events that have already been fetched indirectly from charles"() {
        given:
        bob.sync("b-2", charles)
        alice.sync("a-2", charles)

        when:
        FetchResult result = alice.fetch(bob)

        then:
        result.events == [
                new FetchedEvent("b-1", utf8("b-1")),
                new FetchedEvent("b-2", utf8("b-2"))
        ]
    }

    def "can fetch a great deal of events in chronological order"() {
        given:
        bob.sync("b-2", charles)
        charles.sync("c-2", bob)
        bob.sync("b-3", charles)
        charles.sync("c-3", bob)

        when:
        FetchResult result = alice.fetch(charles)

        then:
        def eventPairs = result.events.collate(2)
        eventPairs[0].containsAll(
                new FetchedEvent("b-1", utf8("b-1")),
                new FetchedEvent("c-1", utf8("c-1"))
        )
        eventPairs[1].containsAll(
                new FetchedEvent("b-2", utf8("b-2")),
                new FetchedEvent("c-2", utf8("c-2"))
        )
        eventPairs[2].containsAll(
                new FetchedEvent("b-3", utf8("b-3")),
                new FetchedEvent("c-3", utf8("c-3"))
        )
    }
}
