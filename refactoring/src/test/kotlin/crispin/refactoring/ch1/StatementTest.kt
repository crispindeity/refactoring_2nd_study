package crispin.refactoring.ch1

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StatementTest :
    FunSpec({
        test("청구서 생성 테스트") {
            // given
            val invoice: Invoice = invoices[0]
            val plays: Map<String, Play> = plays

            // when
            val result: String = statement(invoice, plays)

            // then
            result shouldBe
                """
                청구 내역 (고객명: BigCo)
                    Hamlet: ₩650 (55석)
                    As You Like It: ₩580 (35석)
                    Othello: ₩500 (40석)
                총액: ₩1,730
                적립 포인트: 47점
                
                """.trimIndent()
        }
    })
