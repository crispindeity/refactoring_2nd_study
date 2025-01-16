package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

fun statement(
    invoice: Invoice,
    plays: Map<String, Play>
): String {
    fun playFor(performance: performance) = plays[performance.playID]!!
    var result = "청구 내역 (고객명: ${invoice.customer})\n"

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

    for (performance in invoice.performances) {
        result +=
            "    ${playFor(performance).name}: ${krw(amountFor(performance))} " +
            "(${performance.audience}석)\n"
    }

    /*
    todo    :: 임시 함수 이름 추후 변경
     author :: heechoel shin
     date   :: 2025-01-16T19:0:58KST
     */
    fun tempFunctionName(invoice: Invoice): Int {
        var totalAmount = 0
        for (performance: performance in invoice.performances) {
            totalAmount += amountFor(performance)
        }
        return totalAmount
    }

    fun totalVolumeCredits(): Int {
        var volumeCredits = 0
        for (performance: performance in invoice.performances) {
            volumeCredits += volumeCreditsFor(performance)
        }
        return volumeCredits
    }

    result += "총액: ${krw(tempFunctionName(invoice))}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점\n"
    return result
}

fun main() {
    println(statement(invoice = invoices[0], plays = plays))
}
