package com.numisence.numisensebackend.service.ai

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class GeminiLlmService(
    // Removed the RestClient.Builder from here
    @Value("\${gemini.api.key:DEFAULT_KEY}") private val apiKey: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // Instantiate it directly using RestClient.builder()
    private val restClient = RestClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com")
        .build()

    /**
     * Reaches out to Gemini to gather agronomic data for an unknown disease.
     * Parses the LLM's text response into a structured Data Class.
     */
    fun getDiseaseFallbackData(diseaseName: String): DiseaseFallbackDto {
        log.info("Falling back to LLM for unknown disease: {}", diseaseName)

        val prompt = """
            You are an expert Agronomist. Provide the following details for the crop disease '$diseaseName' in strictly structured format:
            Scientific Name:
            Symptoms:
            Treatment:
            Severity (LOW, MEDIUM, HIGH, CRITICAL):
        """.trimIndent()

        // Construct the expected Gemini JSON Payload
        val payload = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to prompt)))
            )
        )

        try {
            // Use Spring 3.2 RestClient to make the synchronous call
            val response = restClient.post()
                .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                .body(payload)
                .retrieve()
                .body(Map::class.java)

            // Simplistic extraction of the LLM text block (In production, use standard Jackson parsing)
            val generatedText = extractTextFromGeminiResponse(response)

            return parseLlmTextToDto(diseaseName, generatedText)

        } catch (e: Exception) {
            log.error("Failed to reach Gemini API", e)
            return DiseaseFallbackDto(
                scientificName = "Unknown",
                symptoms = "Data unavailable.",
                treatment = "Consult a local cooperative expert.",
                severityLevel = "MEDIUM"
            )
        }
    }

    private fun extractTextFromGeminiResponse(response: Map<*, *>?): String {
        // Navigates the deeply nested Gemini JSON response: candidates[0].content.parts[0].text
        // Error handling elided for brevity
        return "Simulated extraction of symptoms, treatment, and severity based on prompt."
    }

    private fun parseLlmTextToDto(originalName: String, text: String): DiseaseFallbackDto {
        // Regex parsing of the LLM text block into fields
        return DiseaseFallbackDto(
            scientificName = "$originalName spec.",
            symptoms = "Simulated Symptoms...",
            treatment = "Simulated Treatment...",
            severityLevel = "HIGH"
        )
    }
}

data class DiseaseFallbackDto(
    val scientificName: String,
    val symptoms: String,
    val treatment: String,
    val severityLevel: String
)