package uk.co.littlemike.gishgraph.serialization

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.time.Instant

internal data class Thing(val thingId: String, val thingValue: String)

internal data class SomeThings(val id: String, val created: Instant, val things: List<Thing>)

object JacksonSerializerTest : Spek({
    val serializer = JacksonSerializer()

    given("Some composite data") {
        val things = SomeThings("id", Instant.now(), arrayListOf(
                Thing("thing1", "A thing"),
                Thing("thing2", "Another thing")
        ))

        given("serialization") {
            val serializedThings = serializer.serialize(things)

            it("is a string") {
                serializedThings.should.be.instanceof(String::class.java)
            }

            given("deserialization") {
                val deserializedThings: SomeThings = serializer.deserialize(serializedThings)

                it("looks identical to the original object") {
                    deserializedThings.should.equal(things)
                }
            }
        }
    }

})