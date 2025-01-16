package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

fun statement(
    invoice: Invoice,
    plays: Map<String, Play>
): String {
    var totalAmount = 0
    var volumeCredits = 0

    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.KOREA)

    fun amountFor(
        play: Play,
        aPerformance: performance
    ): Int {
        var result: Int
        when (play.type) {
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

            else -> throw IllegalArgumentException("알 수 없는 장르: ${play.type}")
        }
        return result
    }

    for (performance in invoice.performances) {
        val play: Play = plays[performance.playID]!!

        val thisAmount: Int = amountFor(play, performance)

        volumeCredits += maxOf(performance.audience - 30, 0)

        if ("comedy" == play.type) volumeCredits += performance.audience / 5

        result +=
            "    ${play.name}: ${format.format(thisAmount / 100)} (${performance.audience}석)\n"
        totalAmount += thisAmount
    }
    result += "총액: ${format.format(totalAmount / 100)}\n"
    result += "적립 포인트: ${volumeCredits}점\n"
    return result
}

fun main() {
    println(statement(invoice = invoices[0], plays = plays))
}
