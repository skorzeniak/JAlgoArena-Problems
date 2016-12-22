package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.Constants
import jetbrains.exodus.entitystore.Entity

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Problem(val id: String,
              val title: String,
              val description: String,
              val timeLimit: Long,
              val memoryLimit: Int,
              val function: Function?,
              val testCases: Array<TestCase>?,
              val level: Int) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    class TestCase(val input: ArrayNode,
                   val output: JsonNode)

    companion object {
        fun from(entity: Entity): Problem {

            val functionAsJson = entity.getProperty(Constants.problemFunction)
            val function = jacksonObjectMapper().readValue(functionAsJson as String, Function::class.java)

            val testCasesAsJson = entity.getProperty(Constants.problemTestCases)
            val testCases = jacksonObjectMapper().readValue(testCasesAsJson as String, Array<TestCase>::class.java)

            return Problem(
                    entity.getProperty(Constants.problemId) as String,
                    entity.getProperty(Constants.problemTitle) as String,
                    entity.getProperty(Constants.problemDescription) as String,
                    entity.getProperty(Constants.problemTimeLimit) as Long,
                    entity.getProperty(Constants.problemMemoryLimit) as Int,
                    function,
                    testCases,
                    entity.getProperty(Constants.problemLevel) as Int
            )
        }
    }
}
