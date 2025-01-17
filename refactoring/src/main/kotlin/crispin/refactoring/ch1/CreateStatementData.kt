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

open class PerformanceCalculator(
    val aPerformance: Performance,
    val aPlay: Play
) {
    companion object {
        fun createCalculator(
            aPerformance: Performance,
            aPlay: Play
        ): PerformanceCalculator =
            when (aPlay.type) {
                "tragedy" -> TragedyCalculator(aPerformance, aPlay)
                "comedy" -> ComedyCalculator(aPerformance, aPlay)
                else -> throw IllegalArgumentException("알 수 없는 장르: ${aPlay.type}")
            }
    }

    open fun getAmount(): Int = throw IllegalStateException("서브클래스에서 처리하도록 변경되었습니다.")

    fun getVolumeCredits(): Int {
        var volumeCredits = 0
        volumeCredits += maxOf(aPerformance.audience - 30, 0)

        if ("comedy" == aPlay.type) volumeCredits += aPerformance.audience / 5
        return volumeCredits
    }
}

class TragedyCalculator(
    aPerformance: Performance,
    aPlay: Play
) : PerformanceCalculator(aPerformance, aPlay) {
    override fun getAmount(): Int {
        var result = 40000
        if (this.aPerformance.audience > 30) {
            result += 1000 * (this.aPerformance.audience - 30)
        }
        return result
    }
}

class ComedyCalculator(
    aPerformance: Performance,
    aPlay: Play
) : PerformanceCalculator(aPerformance, aPlay) {
    override fun getAmount(): Int {
        var result = 30000
        if (this.aPerformance.audience > 20) {
            result += 10000 + 500 * (this.aPerformance.audience - 20)
        }
        result += 300 * this.aPerformance.audience
        return result
    }
}

fun createStatementData(
    invoice: Invoice,
    plays: Map<String, Play>
): StatementData {
    fun playFor(aPerformance: Performance): Play = plays[aPerformance.playID]!!

    fun totalAmount(data: StatementData): Int = data.performances.sumOf { it.amount }

    fun totalVolumeCredits(data: StatementData): Int = data.performances.sumOf { it.volumeCredits }

    fun enrichPerformance(aPerformance: Performance): EnrichedPerformance {
        val performanceCalculator: PerformanceCalculator =
            PerformanceCalculator.createCalculator(aPerformance, playFor(aPerformance))
        return EnrichedPerformance(
            aPerformance.playID,
            aPerformance.audience,
            performanceCalculator.aPlay
        ).apply {
            amount = performanceCalculator.getAmount()
            volumeCredits = performanceCalculator.getVolumeCredits()
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
