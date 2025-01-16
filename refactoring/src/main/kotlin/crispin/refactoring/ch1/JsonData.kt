package crispin.refactoring.ch1

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val playsJson =
    """
    {
      "hamlet": {"name": "Hamlet", "type": "tragedy"},
      "as-like": {"name": "As You Like It", "type": "comedy"},
      "othello": {"name": "Othello", "type": "tragedy"}
    }
    """.trimIndent()

private val invoicesJson =
    """
    [
      {
        "customer": "BigCo",
        "performances": [
          {
            "playID": "hamlet",
            "audience": 55
          },
          {
            "playID": "as-like",
            "audience": 35
          },
          {
            "playID": "othello",
            "audience": 40
          }
        ]
      }
    ]
    """.trimIndent()

@Serializable
data class Play(
    val name: String,
    val type: String
)

@Serializable
data class Performance(
    val playID: String,
    val audience: Int
)

@Serializable
data class Invoice(
    val customer: String,
    val performances: List<Performance>
)

private val json = Json { ignoreUnknownKeys = true }

val plays: Map<String, Play> = json.decodeFromString(playsJson)
val invoices: List<Invoice> = json.decodeFromString(invoicesJson)
