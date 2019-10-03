package siarhei.luskanau.iot.doorbell.data.repository

import kotlin.test.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private const val MAX_COUNT = 100
private const val SIZE = 20

object DoorbellRepositoryFakeTest : Spek({

    val doorbellRepositoryFake by memoized { DoorbellRepositoryFake() }

    describe("check getFromToRange()") {

        listOf(
                FromToRangeData(
                        expected = Pair(0, 19),
                        startAt = null,
                        orderAsc = true
                ),
                FromToRangeData(
                        expected = Pair(19, 0),
                        startAt = null,
                        orderAsc = false
                ),
                FromToRangeData(
                        expected = Pair(11, 30),
                        startAt = "10",
                        orderAsc = true
                ),
                FromToRangeData(
                        expected = Pair(9, 0),
                        startAt = "10",
                        orderAsc = false
                ),
                FromToRangeData(
                        expected = Pair(91, 99),
                        startAt = "90",
                        orderAsc = true
                ),
                FromToRangeData(
                        expected = Pair(89, 70),
                        startAt = "90",
                        orderAsc = false
                )
        ).forEach { data ->
            context("$data") {
                var actual: Pair<Int, Int>? = null

                beforeEachTest {
                    actual = doorbellRepositoryFake.getFromToRange(
                            size = data.size,
                            startAt = data.startAt,
                            orderAsc = data.orderAsc,
                            maxCount = data.maxCount
                    )
                }

                it("actual=$actual expected=$data") {
                    assertEquals(expected = data.expected, actual = actual)
                }
            }
        }
    }
})

private data class FromToRangeData(
    val expected: Pair<Int, Int>,
    val size: Int = SIZE,
    val startAt: String?,
    val orderAsc: Boolean,
    val maxCount: Int = MAX_COUNT
)
