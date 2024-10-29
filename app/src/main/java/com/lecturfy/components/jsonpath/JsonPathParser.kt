package com.lecturfy.components.jsonpath

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.spi.json.JacksonJsonProvider

data class DomainObject(
    val properties: List<DomainObjectProperty>
)

data class DomainObjectProperty(
    val type: String,
    val jsonPath: String,
    val isOptional: Boolean,
    val name: String
)

class JsonPathParser {

    fun <T : Any> parseJsonToDomainObject(
        json: String,
        domainObject: DomainObject,
        clazz: Class<T>,
        parseAsList: Boolean = true
    ): List<T> {
        val objectMapper = jacksonObjectMapper()
        val valueConf = createConfiguration()

        val constantValuesMap = parseConstantValues(domainObject, json, valueConf)
        val dynamicValuesMap = parseDynamicValues(domainObject, json, valueConf)

        return if (parseAsList) {
            parseAsList(constantValuesMap, dynamicValuesMap, domainObject, clazz, objectMapper)
        } else {
            listOf(parseAsEntity(constantValuesMap, dynamicValuesMap, domainObject, clazz, objectMapper))
        }
    }

    private fun createConfiguration(): Configuration {
        return Configuration.builder()
            .jsonProvider(JacksonJsonProvider())
            .options(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .build()
    }

    private fun parseConstantValues(
        domainObject: DomainObject,
        json: String,
        valueConf: Configuration
    ): MutableMap<String, Any?> {
        val constantValuesMap = mutableMapOf<String, Any?>()

        domainObject.properties.forEach { property ->
            if (property.jsonPath.contains(Regex("\\[\\d+\\]")) && !property.jsonPath.contains("[*]")) {
                val constantValue = JsonPath.using(valueConf).parse(json).read<Any?>(property.jsonPath)

                if (constantValue == null && !property.isOptional) {
                    throw IllegalArgumentException("Required property '${property.name}' is missing")
                }

                constantValuesMap[property.name] = constantValue
            }
        }
        return constantValuesMap
    }

    private fun parseDynamicValues(
        domainObject: DomainObject,
        json: String,
        valueConf: Configuration
    ): MutableMap<String, List<Any?>> {
        val dynamicValuesMap = mutableMapOf<String, List<Any?>>()

        domainObject.properties.forEach { property ->
            if (!property.jsonPath.contains(Regex("\\[\\d+\\]")) || property.jsonPath.contains("[*]")) {
                val dynamicValues = JsonPath.using(valueConf).parse(json).read<List<Any?>>(property.jsonPath)
                dynamicValuesMap[property.name] = dynamicValues
            }
        }
        return dynamicValuesMap
    }

    private fun <T : Any> parseAsList(
        constantValuesMap: Map<String, Any?>,
        dynamicValuesMap: Map<String, List<Any?>>,
        domainObject: DomainObject,
        clazz: Class<T>,
        objectMapper: ObjectMapper
    ): List<T> {
        val maxCount = dynamicValuesMap.values.map { it.size }.maxOrNull() ?: 1
        val parsedResults = mutableListOf<T>()

        for (i in 0 until maxCount) {
            val instanceMap = buildInstanceMap(constantValuesMap, dynamicValuesMap, domainObject, i)
            val parsedResultJson = objectMapper.writeValueAsString(instanceMap)
            val parsedObject = objectMapper.readValue(parsedResultJson, clazz)
            parsedResults.add(parsedObject)
        }

        return parsedResults
    }

    private fun <T : Any> parseAsEntity(
        constantValuesMap: Map<String, Any?>,
        dynamicValuesMap: Map<String, List<Any?>>,
        domainObject: DomainObject,
        clazz: Class<T>,
        objectMapper: ObjectMapper
    ): T {
        val instanceMap = buildInstanceMap(constantValuesMap, dynamicValuesMap, domainObject, 0)
        val parsedResultJson = objectMapper.writeValueAsString(instanceMap)
        return objectMapper.readValue(parsedResultJson, clazz)
    }

    private fun buildInstanceMap(
        constantValuesMap: Map<String, Any?>,
        dynamicValuesMap: Map<String, List<Any?>>,
        domainObject: DomainObject,
        index: Int
    ): MutableMap<String, Any?> {
        val instanceMap = mutableMapOf<String, Any?>()

        domainObject.properties.forEach { property ->
            val value = constantValuesMap[property.name] ?: dynamicValuesMap[property.name]?.getOrNull(index)

            if (value == null && !property.isOptional) {
                throw IllegalArgumentException("Required property '${property.name}' is missing at index $index")
            }

            instanceMap[property.name] = value
        }

        return instanceMap
    }

    fun <T : Any> parseEntity(
        json: String,
        domainObject: DomainObject,
        clazz: Class<T>
    ): T {
        val parsedList = parseJsonToDomainObject(json, domainObject, clazz, parseAsList = false)
        return parsedList.first()
    }

    fun <T : Any> parseList(
        json: String,
        domainObject: DomainObject,
        clazz: Class<T>
    ): List<T> {
        return parseJsonToDomainObject(json, domainObject, clazz, parseAsList = true)
    }
}
