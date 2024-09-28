package roomescape

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import roomescape.assertion.assertHashcode

class Step3 {

    @Test
    fun `코루틴 예외 전파`() = runTest {
        // given
        val actual: StringBuilder = StringBuilder()

        // when
        val job = launch {
            try {
                launch {
                    delay(150)
                    actual.append(1)
                }
                supervisorScope {
                    val deferred = async {
                        delay(100)
                        throw RuntimeException("E2")
                    }
                    launch {
                        delay(200)
                        throw RuntimeException("E3")
                    }
                    deferred.await()
                }
            } catch (e: Exception) {
                actual.append(e.message)
            }
        }
        job.join()

        // then
        val expected = "E21" // TODO: 결과값 예상
        /*
            TODO: 간단한 풀이과정 작성
            100ms를 기다려서, E2가 append되었고, supervisorScope라서, 형제인 launch가 취소되지 않아, 150ms 후 1이 append 되었음.
         */

        // assert문 수정하지 마세요!
        assertHashcode(actual, expected)
    }
}
