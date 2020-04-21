import org.junit.jupiter.api.Test

class ParserTest {
    private val parser = TorrentParser()
    private val simpleDictTorrent = "d3:cow3:moo4:spam4:eggse"
    private val simpleDictRes = mapOf<String, String>("cow" to "moo", "spam" to "eggs")
    private val dictWithIntsTorrent = "d3:onei1e7:-twelvei-12ee"
    private val dictWithIntsRes = mapOf<String, Int>("one" to 1, "-twelve" to -12)
    private val dictOfDictsTorrent = ("d3:one"+simpleDictTorrent+ "3:two"+dictWithIntsTorrent+"e")
    private val dictOfDictsRes = mapOf<String, Any>("one" to simpleDictRes, "two" to dictWithIntsRes)
    private val dictWithListTorrent = "d4:spaml1:a1:bee"
    private val dictWithListRes = mapOf<String, Any>("spam" to listOf("a", "b"))
    private val emptyDictTorrent = "de"
    private val emptyDictRes = mapOf<String, Any>()
    private val dictWithEmptyListTorrent = "d3:onelee"
    private val dictWithEmptyListRes = mapOf<String, Any>("one" to listOf<Any>())
    private val dictWithLongIntTorrent = "d1:ai123ee"
    private val dictWithLongIntRes = mapOf<String, Any>("a" to 123)

    @Test
    fun `empty dict`() {
        val dict = parser.parse(emptyDictTorrent.toByteArray())

        assert(dict == emptyDictRes)
    }

    @Test
    fun `full check simple strings dict`() {
        val dict = parser.parse(simpleDictTorrent.toByteArray())

        assert(dict == simpleDictRes)
    }

    @Test
    fun `dict with int`() {
        val dict = parser.parse(dictWithIntsTorrent.toByteArray())

        assert(dict == dictWithIntsRes)
    }

    @Test
    fun `dict with long single int`() {
        val dict = parser.parse(dictWithLongIntTorrent.toByteArray())

        assert(dict == dictWithLongIntRes)
    }

    @Test
    fun `dict of dicts`() {
        val dict = parser.parse(dictOfDictsTorrent.toByteArray())

        assert(dict == dictOfDictsRes)
    }

    @Test
    fun `dict with list`() {
        val dict = parser.parse(dictWithListTorrent.toByteArray())

        assert(dict == dictWithListRes)
    }

    @Test
    fun `dict with empty list`() {
        val dict = parser.parse(dictWithEmptyListTorrent.toByteArray())

        assert(dict == dictWithEmptyListRes)
    }
}