package roomescape.bonus

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import roomescape.assertion.assertHashcode

class BonusStep1 {

    @Test
    fun `SharedFlow replay, Buffer`() = runTest {
        // given
        val actual: StringBuilder = StringBuilder("")
        val sharedFlow = MutableSharedFlow<Int>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST,
        )

        // when
        val emitterJob = launch {
            sharedFlow.emit(1)//0ms
            delay(100)
            sharedFlow.emit(2)//100ms
            delay(100)
            sharedFlow.emit(3)//200ms
            sharedFlow.emit(4)//200ms
        }

        delay(50)
        val collectorJob1 = launch {
            sharedFlow.collect(actual::append)//234
        }

        delay(150)
        val collectorJob2 = launch {
            sharedFlow.collect(actual::append)//34
        }

        delay(250) // 450
        val collectorJob3 = launch {
            sharedFlow.collect(actual::append)//
        }

        emitterJob.join()
        collectorJob1.cancelAndJoin()
        collectorJob2.cancelAndJoin()
        collectorJob3.cancelAndJoin()

        // then
        val expected = "12333444" // TODO: 결과값 예상
        /* TODO: 간단한 풀이과정 작성

         */

        // assert문 수정하지 마세요!
        assertHashcode(actual, expected)
    }
}
