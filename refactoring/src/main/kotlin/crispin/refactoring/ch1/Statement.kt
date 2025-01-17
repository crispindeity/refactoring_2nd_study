package crispin.refactoring.ch1

import java.text.NumberFormat
import java.util.Locale

class Statement(
    val invoice: Invoice,
    val plays: Map<String, Play>
) {
    private val data: StatementData = CreateStatementData(invoice, plays).getStatementData()

    fun renderPlainText(): String {
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

    fun renderHtml(): String {
        var result = "<h1>청구 내역 (고객명: ${data.customer})</h1>\n"
        result += "<table>\n"
        result += "<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>"
        for (performance: EnrichedPerformance in data.performances) {
            result += "    <tr><td>${performance.play.name}</td><td>(${performance.audience}석)</td>"
            result += "<td>${krw(performance.amount)}</td></tr>\n"
        }
        result += "</table>\n"
        result += "<p>총액: <em>${krw(data.totalAmount)}</em></p>\n"
        result += "<p>적립 포인트: <em>${data.totalVolumeCredits}</em>점</p>\n"
        return result
    }

    private fun krw(aNumber: Int): String? =
        NumberFormat
            .getCurrencyInstance(Locale.KOREA)
            .format(aNumber / 100)
}

fun main() {
    val statement = Statement(invoice = invoices[0], plays = plays)
    println(statement.renderPlainText())
    println(statement.renderHtml())
}
