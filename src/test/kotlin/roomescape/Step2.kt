package roomescape

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import roomescape.assertion.assertHashcode

class Step2 {

    @Test
    fun `SharedFlow replay`() = runTest {
        // given
        val actual: StringBuilder = StringBuilder()
        val sharedFlow = MutableSharedFlow<Int>(replay = 1)

        // when
        val emitterJob = launch {
            sharedFlow.emit(1)
            delay(100)
            sharedFlow.emit(2)
            delay(100)
            sharedFlow.emit(3)
            delay(100)
            sharedFlow.emit(4)
        }

        val collectorJob1 = launch {
            sharedFlow.collect(actual::append)//1부터 수집
        }

        delay(150)
        val collectorJob2 = launch {
            sharedFlow.collect(actual::append)//2부터 수집
        }

        emitterJob.join()
        collectorJob1.cancelAndJoin()
        collectorJob2.cancelAndJoin()

        // then
        val expected = "1223344" // TODO: 결과값 예상
        /*
            TODO: 간단한 풀이과정 작성
            collectorJob1는 1부터 수집하고, collectorJob2는 2부터 수집함.
         */

        // assert문 수정하지 마세요!
        assertHashcode(actual, expected)
    }
}
