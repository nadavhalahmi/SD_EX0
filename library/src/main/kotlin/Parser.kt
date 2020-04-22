import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//TODO: replace Any with union of dict, list, int, string

class TorrentParser {
    private val charset = Charsets.UTF_8

    fun SHAsum(convertme: ByteArray) : String{
        val md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(convertme));
    }

    fun byteArray2Hex(hash: ByteArray) : String{
        val formatter = Formatter();
        for (b in hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     * [torrent]: the torrent to parse
     * [startIndex]: the start index of the element
     * [stopCond]: the condition for stop. depends on current index
     * @return: element, element's length
     */
    private fun parseElement(torrent: ByteArray, startIndex: Int, stopCond: (Int) -> Boolean): Pair<String, Int> {
        var key = ""
        var len = 0
        var index = startIndex
        while(!stopCond(index)) {
            key += torrent[index].toChar()
            len++
            index++
        }
        return Pair(key, len)
    }

    private fun parseValue(torrent: ByteArray, startIndex: Int): Pair<Any, Int>{
        val res: Pair<Any, Int>
        when(torrent[startIndex].toChar()) {
            in '0'..'9' -> {
                res = parseNumElemPair(torrent, startIndex)
            }
            'i' -> {
                res = parseInt(torrent, startIndex)
            }
            'l' -> {
                res = parseList(torrent, startIndex)
            }
            'd' -> {
                res = parseDict(torrent, startIndex)
            }
            else -> {
                throw Exception("Invalid Torrent")
            }
        }
        return Pair(res.first, res.second)
    }


    private fun parseNumElemPair(torrent: ByteArray, index: Int): Pair<String, Int> {
        assert(torrent[index].toChar() in '1'..'9')
        var pairLen = 0
        var res: Pair<String, Int> = parseElement(torrent, index) {torrent[it].toChar() == ':'}
        //use val elemLen = res.first.toString(charset).toInt() if res.first is ByteArray
        val elemLen = res.first.toInt()
        val elemStartIndex = index+res.second+1 //pass elemLen and ':' sign
        pairLen += res.second+1
        res = parseElement(torrent, elemStartIndex) { it-elemStartIndex >= elemLen } //parse key
        val elem = res.first
        pairLen += res.second
        return Pair(elem, pairLen)
    }
    private fun parseList(torrent: ByteArray, startIndex: Int): Pair<List<Any>, Int>{
        assert(torrent[startIndex].toChar() == 'l')
        val lst = ArrayList<Any>()
        var index = startIndex+1 //pass 'l'
        var res: Pair<Any, Int>
        var len = 1 //for 'l'
        while(torrent[index].toChar() != 'e'){
            res = parseValue(torrent, index)
            lst.add(res.first)
            index += res.second
            len += res.second
        }
        assert(torrent[index].toChar() == 'e')
        len++ //pass 'e'
        return Pair(lst, len)
    }

    private fun parseDict(torrent: ByteArray, startIndex: Int): Pair<HashMap<String, Any>, Int>{
        assert(torrent[startIndex].toChar() == 'd')
        val dict = HashMap<String, Any>()
        var index = startIndex+1
        var key: String
        var res: Pair<Any, Int>
        var len = 1
        while(torrent[index].toChar() != 'e'){
            when(torrent[index].toChar()){
                in '0'..'9' -> {
                    res = parseNumElemPair(torrent, index)
                    key = res.first
                    index += res.second
                    len += res.second
                }
                else -> throw Exception("DictKeyShouldBeString")
            }
            res = parseValue(torrent, index)
            dict[key] = res.first
            index += res.second
            len += res.second
        }
        assert(torrent[index].toChar() == 'e')
        len++ //pass 'e'
        return Pair(dict, len)
    }


    private fun parseInt(torrent: ByteArray, startIndex: Int): Pair<Int, Int> {
        assert(torrent[startIndex].toChar() == 'i')
        val res = parseElement(torrent, startIndex+1) {torrent[it].toChar() == 'e'}
        return Pair(res.first.toInt(), res.second+2) //+2 for i and e
        //use res.first.toString(charset).toInt() in order to get int value
    }


    fun parse(torrent: ByteArray): HashMap<String, Any>{
        when(torrent[0].toChar()){
            'd' -> return parseDict(torrent, 0).first
        }
        return hashMapOf()
    }

    fun encode(torrent: Any?): ByteArray {
        var res: ByteArray
        when(torrent){
             is Map<*,*> -> {
                res = "d".toByteArray()
                for (it in torrent){
                    res += encode(it.key)
                    res += encode(it.value)
                }
                res += "e".toByteArray()
            }
            is List<*> -> {
                res = "l".toByteArray()
                for (it in torrent) {
                    res += encode(it)
                }
                res += "e".toByteArray()
            }
            is Int ->{
                res = "i".toByteArray()
                res += torrent.toString().toByteArray()
                res += "e".toByteArray()
            }
            is String ->{
                res = torrent.length.toString().toByteArray()
                res += ":".toByteArray()
                res += torrent.toByteArray()
            }
            else ->{
                throw Exception("encode error")
            }
        }
        return res
    }
}