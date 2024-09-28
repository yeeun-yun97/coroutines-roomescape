package roomescape

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import roomescape.assertion.assertHashcode

class Step1 {

    @Test
    fun `종류별 Scope`() = runTest {
        // given
        val actual: StringBuilder = StringBuilder()

        // when
        val deferred = async {
            delay(500)
            actual.append(1)
        }
        launch {
            delay(200)
            actual.append(2)
        }
        coroutineScope {
            launch {
                delay(300)
                actual.append(3)
            }
            actual.append(4)
        }
        deferred.await()
        actual.append(5)

        // then
        val expected = "42315" // TODO: 결과값 예상
        /*
            TODO: 간단한 풀이과정 작성
            delay를 기다리지 않고, deferred를 기다리지 않는 4가 제일 먼저,
            200ms를 기다리는 2가 그 다음,
            300ms를 기다리는 3이 그 다음,
            500ms를 기다리는 1이 그 다음(deferred),
            deferred를 기다린 5가 그 다음에 실행된다.
         */

        // assert문 수정하지 마세요!
        assertHashcode(actual, expected)
    }
}
