package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

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

fun statement(
    invoice: Invoice,
    plays: Map<String, Play>
): String {
    fun playFor(aPerformance: Performance): Play = plays[aPerformance.playID]!!

    fun amountFor(aPerformance: EnrichedPerformance): Int {
        var result: Int
        when (aPerformance.play.type) {
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

            else -> throw IllegalArgumentException("알 수 없는 장르: ${aPerformance.play.type}")
        }
        return result
    }

    fun volumeCreditsFor(aPerformance: EnrichedPerformance): Int {
        var volumeCredits = 0
        volumeCredits += maxOf(aPerformance.audience - 30, 0)

        if ("comedy" == aPerformance.play.type) volumeCredits += aPerformance.audience / 5
        return volumeCredits
    }

    fun totalAmount(data: StatementData): Int = data.performances.sumOf { it.amount }

    fun totalVolumeCredits(data: StatementData): Int {
        var result = 0
        for (performance: EnrichedPerformance in data.performances) {
            result += performance.volumeCredits
        }
        return result
    }

    fun enrichPerformance(aPerformance: Performance): EnrichedPerformance =
        EnrichedPerformance(
            aPerformance.playID,
            aPerformance.audience,
            playFor(aPerformance)
        ).apply {
            amount = amountFor(this)
            volumeCredits = volumeCreditsFor(this)
        }

    val statementData =
        StatementData(
            invoice.customer,
            invoice.performances.map { enrichPerformance(it) }
        ).apply {
            totalAmount = totalAmount(this)
            totalVolumeCredits = totalVolumeCredits(this)
        }

    return renderPlainText(statementData)
}

private fun renderPlainText(data: StatementData): String {
    fun krw(aNumber: Int): String? =
        NumberFormat
            .getCurrencyInstance(Locale.KOREA)
            .format(aNumber / 100)

    var result = "청구 내역 (고객명: ${data.customer})\n"
    for (performance: EnrichedPerformance in data.performances) {
        result +=
            "    ${performance.play.name}: ${krw(performance.amount)} " +
            "(${performance.audience}석)\n"
    }

    result += "총액: ${krw(data.totalAmount)}\n"
    result += "적립 포인트: ${data.totalVolumeCredits}점\n"
    return result
}

fun main() {
    println(statement(invoice = invoices[0], plays = plays))
}
