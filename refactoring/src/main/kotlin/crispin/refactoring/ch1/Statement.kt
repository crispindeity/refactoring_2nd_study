package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

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

fun statement(
    invoice: Invoice,
    plays: Map<String, Play>
): String = renderPlainText(createStatementData(invoice, plays))

fun main() {
    println(statement(invoice = invoices[0], plays = plays))
}
