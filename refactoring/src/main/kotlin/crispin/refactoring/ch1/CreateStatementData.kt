package crispin.refactoring.ch1

data class StatementData(
    val customer: String,
    val performances: List<EnrichedPerformance>
) {
    var totalAmount: Int = 0
    var totalVolumeCredits: Int = 0
}

data class EnrichedPerformance(
    val playID: String,
    val audience: Int,
    val play: Play
) {
    var amount: Int = 0
    var volumeCredits: Int = 0
}

class PerformanceCalculator(
    val aPerformance: Performance,
    val aPlay: Play
) {
    fun getAmount(): Int {
        var result: Int
        when (aPlay.type) {
            "tragedy" -> {
                result = 40000
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30)
                }
            }

            "comedy" -> {
                result = 30000
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20)
                }
                result += 300 * aPerformance.audience
            }

            else -> throw IllegalArgumentException("알 수 없는 장르: ${aPlay.type}")
        }
        return result
    }
}

fun createStatementData(
    invoice: Invoice,
    plays: Map<String, Play>
): StatementData {
    fun playFor(aPerformance: Performance): Play = plays[aPerformance.playID]!!

    fun volumeCreditsFor(aPerformance: EnrichedPerformance): Int {
        var volumeCredits = 0
        volumeCredits += maxOf(aPerformance.audience - 30, 0)

        if ("comedy" == aPerformance.play.type) volumeCredits += aPerformance.audience / 5
        return volumeCredits
    }

    fun totalAmount(data: StatementData): Int = data.performances.sumOf { it.amount }

    fun totalVolumeCredits(data: StatementData): Int = data.performances.sumOf { it.volumeCredits }

    fun enrichPerformance(aPerformance: Performance): EnrichedPerformance {
        val performanceCalculator = PerformanceCalculator(aPerformance, playFor(aPerformance))
        return EnrichedPerformance(
            aPerformance.playID,
            aPerformance.audience,
            performanceCalculator.aPlay
        ).apply {
            amount = performanceCalculator.getAmount()
            volumeCredits = volumeCreditsFor(this)
        }
    }

    val statementData =
        StatementData(
            invoice.customer,
            invoice.performances.map { enrichPerformance(it) }
        ).apply {
            totalAmount = totalAmount(this)
            totalVolumeCredits = totalVolumeCredits(this)
        }

    return statementData
}
