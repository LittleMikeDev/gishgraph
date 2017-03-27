package uk.co.littlemike.gishgraph.serialization

import groovy.transform.EqualsAndHashCode
import spock.lang.Specification

import java.time.Instant

@EqualsAndHashCode
class Thing {
    String thingId
    String thingValue
}

@EqualsAndHashCode
class SomeThings {
    String id
    Instant created
    List<Thing> things
}

class JacksonSerializerTest extends Specification {

    def things = new SomeThings([
            id: "abc",
            created: Instant.now(),
            things: [
                    new Thing([thingId: "thing1", thingValue: "value1"]),
                    new Thing([thingId: "thing2", thingValue: "value2"])
            ]
    ])

    def serializer = new JacksonSerializer()

    def "it serializes to some sort of string"() {
        when:
        def serializedThings = serializer.serialize(things)

        then:
        serializedThings instanceof String
    }

    def "the deserialized object looks identical to the original"() {
        given:
        def serializedThings = serializer.serialize(things)

        when:
        def deserializedThings = serializer.deserialize(SomeThings.class, serializedThings)

        then:
        deserializedThings == things
    }
}
