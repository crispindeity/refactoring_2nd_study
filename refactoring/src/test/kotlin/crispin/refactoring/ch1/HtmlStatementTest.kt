package crispin.refactoring.ch1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HtmlStatementTest :
    FunSpec({
        test("청구서 Html 생성 테스트") {
            // given
            val invoice: Invoice = invoices[0]
            val plays: Map<String, Play> = plays
            val statement = Statement(invoice, plays)

            // when
            val result: String = statement.renderHtml()

            // then
            result shouldBe
                """
                <h1>청구 내역 (고객명: BigCo)</h1>
                <table>
                <tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>    <tr><td>Hamlet</td><td>(55석)</td><td>₩650</td></tr>
                    <tr><td>As You Like It</td><td>(35석)</td><td>₩580</td></tr>
                    <tr><td>Othello</td><td>(40석)</td><td>₩500</td></tr>
                </table>
                <p>총액: <em>₩1,730</em></p>
                <p>적립 포인트: <em>47</em>점</p>
                
                """.trimIndent()
        }
    })
