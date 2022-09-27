import kotlin.test.*

/**
 * Implements tests for [Ybor].
 */
class YborTest {

    @Test
    fun `test exec with multi line`() {
        Ybor.exec("1+2")
    }

}