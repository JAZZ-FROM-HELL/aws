package org.example

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.google.gson.Gson

data class RequestInput(
    val name: String? = null,
    val message: String? = null
)

data class ResponseOutput(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json")
)

class Main : RequestHandler<RequestInput, ResponseOutput> {

    private val gson = Gson()

    override fun handleRequest(input: RequestInput, context: Context): ResponseOutput {
        val logger = context.logger
        logger.log("Processing request with input: ${gson.toJson(input)}")

        val name = input.name ?: "World"
        val customMessage = input.message ?: "Hello"

        val responseBody = mapOf(
            "greeting" to "$customMessage, $name!",
            "timestamp" to System.currentTimeMillis(),
            "requestId" to context.awsRequestId
        )

        logger.log("Returning response: ${gson.toJson(responseBody)}")

        return ResponseOutput(
            statusCode = 200,
            body = gson.toJson(responseBody)
        )
    }
}
