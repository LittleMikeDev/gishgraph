package uk.co.littlemike.gishgraph.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JacksonSerializer {
    val mapper : ObjectMapper = ObjectMapper().findAndRegisterModules()

    fun serialize(data: Any) = mapper.writeValueAsString(data)

    inline fun <reified T: Any> deserialize(serializedData: String): T = mapper.readValue(serializedData)

    fun <T: Any> deserialize(clazz: Class<T>, serializedData: String) = mapper.readValue(serializedData, clazz)

}
