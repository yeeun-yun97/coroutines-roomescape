package roomescape

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import roomescape.assertion.assertHashcode

class Step4 {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `StateFlow와 SharedFlow`() = runTest {
        // given
        val actual: StringBuilder = StringBuilder()

        val a = MutableStateFlow(1)
        val b = MutableStateFlow(true)
        val c = MutableSharedFlow<Boolean>()

        // when
        val collectorJob = launch {
            a
                .flatMapLatest { b.filter { it }/*b가 트루일 때에만 emit한다?*/ }
                .flatMapLatest { c.filter { it }/*c가 트루일 때 emit한다?*/ }
                .onEach { actual.append(it) }
                .collect()
        }
        val emitterJob = launch {
            delay(100)
            c.emit(true)// 1, true, true
            b.value = false//1, false, true

            a.value = 10//10, false, true

            c.emit(false)//10, false, false
            b.value = true//10, true, false

            a.value = 5//5, true, false
        }
        emitterJob.join()
        collectorJob.cancelAndJoin()

        // then
        val expected = "true" // TODO: 결과값 예상
        /*
            TODO: 간단한 풀이과정 작성
            a가 바뀔 때, b가 true이면 true를 emit하는 flow가 바뀔 때 c가 true이면 true를 emit하는 flow가 바뀔 때 actual에 emit 하게 된다.
            최초 상태에서 1, true, true이므로 true가 1회 emit 된다.
            그 이후 b가 false가 되어, a가 10으로 바뀌었으나 emit이 되지 않는다.
            그 이후 c는 false가 되고, b는 true가 되어 a가 5로 바뀌었으나 emit이 되지 않는다.
            그래서 최초 한번만 작동하여 값은 true가 된다.
         */

        // assert문 수정하지 마세요!
        assertHashcode(actual, expected)
    }
}
