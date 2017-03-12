package uk.co.littlemike.gishgraph.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JacksonSerializer {
    val mapper : ObjectMapper = ObjectMapper().findAndRegisterModules()

    fun serialize(data: Any): String {
        return mapper.writeValueAsString(data)
    }

    inline fun <reified T: Any> deserialize(serializedData: String): T {
        return mapper.readValue(serializedData)
    }

}
