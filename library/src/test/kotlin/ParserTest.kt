import org.junit.jupiter.api.Test

class ParserTest {
    private val parser = TorrentParser()
    private val simpleDict = "d3:cow3:moo4:spam4:eggse".toByteArray()

    @Test
    fun `full check simple strings dict`() {
        val dict = parser.parse(simpleDict)

        assert(dict == mapOf<String, String>("cow" to "moo", "spam" to "eggs"))
    }

}