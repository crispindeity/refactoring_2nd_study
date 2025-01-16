package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

fun statement(
    invoice: Invoice,
    plays: Map<String, Play>
): String = renderPlainText(plays, invoice)

private fun renderPlainText(
    plays: Map<String, Play>,
    invoice: Invoice
): String {
    fun playFor(performance: performance) = plays[performance.playID]!!

    fun krw(aNumber: Int): String? =
        NumberFormat
            .getCurrencyInstance(Locale.KOREA)
            .format(aNumber / 100)

    fun amountFor(aPerformance: performance): Int {
        var result: Int
        when (playFor(aPerformance).type) {
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

            else -> throw IllegalArgumentException("알 수 없는 장르: ${playFor(aPerformance).type}")
        }
        return result
    }

    fun volumeCreditsFor(aPerformance: performance): Int {
        var volumeCredits = 0
        volumeCredits += maxOf(aPerformance.audience - 30, 0)

        if ("comedy" == playFor(aPerformance).type) volumeCredits += aPerformance.audience / 5
        return volumeCredits
    }

    fun totalAmount(invoice: Invoice): Int {
        var result = 0
        for (performance: performance in invoice.performances) {
            result += amountFor(performance)
        }
        return result
    }

    fun totalVolumeCredits(): Int {
        var result = 0
        for (performance: performance in invoice.performances) {
            result += volumeCreditsFor(performance)
        }
        return result
    }

    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    for (performance in invoice.performances) {
        result +=
            "    ${playFor(performance).name}: ${krw(amountFor(performance))} " +
            "(${performance.audience}석)\n"
    }

    result += "총액: ${krw(totalAmount(invoice))}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점\n"
    return result
}

fun main() {
    println(statement(invoice = invoices[0], plays = plays))
}
