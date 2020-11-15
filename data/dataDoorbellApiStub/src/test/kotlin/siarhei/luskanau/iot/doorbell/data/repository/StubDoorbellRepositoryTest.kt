package siarhei.luskanau.iot.doorbell.data.repository

import kotlin.test.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private const val MIN_COUNT = 0
private const val MAX_COUNT = 100
private const val SIZE = 20

object StubDoorbellRepositoryTest : Spek({

    val stubDoorbellRepository by memoized { StubDoorbellRepository() }

    describe("check getFromToRange()") {

        listOf(
            FromToRangeData(
                startAt = null,
                orderAsc = true,
                expected = Pair(0, 19)
            ),
            FromToRangeData(
                startAt = null,
                orderAsc = false,
                expected = Pair(19, 0)
            ),
            FromToRangeData(
                startAt = "10",
                orderAsc = true,
                expected = Pair(11, 30)
            ),
            FromToRangeData(
                startAt = "10",
                orderAsc = false,
                expected = Pair(9, 0)
            ),
            FromToRangeData(
                startAt = "1",
                orderAsc = false,
                expected = Pair(0, 0)
            ),
            FromToRangeData(
                startAt = "0",
                orderAsc = false,
                expected = null
            ),
            FromToRangeData(
                startAt = "-1",
                orderAsc = false,
                expected = null
            ),
            FromToRangeData(
                startAt = "90",
                orderAsc = true,
                expected = Pair(91, 99)
            ),
            FromToRangeData(
                startAt = "90",
                orderAsc = false,
                expected = Pair(89, 70)
            ),
            FromToRangeData(
                startAt = "99",
                orderAsc = true,
                expected = null
            ),
            FromToRangeData(
                startAt = "100",
                orderAsc = true,
                expected = null
            )
        ).forEach { data ->
            context("$data") {
                var actual: Pair<Int, Int>? = null

                beforeEachTest {
                    actual = stubDoorbellRepository.getFromToRange(
                        startAt = data.startAt?.toInt(),
                        orderAsc = data.orderAsc,
                        minCount = MIN_COUNT,
                        maxCount = MAX_COUNT,
                        size = SIZE
                    )
                }

                it("check the $data") {
                    assertEquals(
                        expected = data.expected,
                        actual = actual,
                        message = "actual=$actual expected=$data"
                    )
                }
            }
        }
    }
})

private data class FromToRangeData(
    val startAt: String?,
    val orderAsc: Boolean,
    val expected: Pair<Int, Int>?
)
