class TorrentParser {
    fun parseElement(torrent: ByteArray, i: Int, stopCond: (Int) -> Boolean): Pair<String, Int> {
        var key = ""
        var len = 0
        var index = i
        while(!stopCond(index)) {
            key += torrent[index].toChar()
            len++
            index++
        }
        return Pair(key, len)
    }

    fun parseNumElemPair(torrent: ByteArray, index: Int): Pair<String, Int> {
        var pairLen = 0
        var res: Pair<String, Int> = parseElement(torrent, index) {torrent[it].toChar() == ':'}
        val elemLen = res.first.toInt()
        val elemStartIndex = index+res.second+1 //pass elemLen and ':' sign
        pairLen += res.second+1
        res = parseElement(torrent, elemStartIndex) { it-elemStartIndex >= elemLen } //parse key
        val elem = res.first
        pairLen += res.second
        return Pair(elem, pairLen)
    }

    fun parseDict(torrent: ByteArray, startIndex: Int): HashMap<String, String>{
        val dict = HashMap<String, String>() //TODO: fix second type to Union of dict, list, int, string
        var index = startIndex
        var key: String = ""
        var value: String = ""
        var res: Pair<String, Int>
        while(torrent[index].toChar() != 'e'){
            when(torrent[index].toChar()){
                in '0'..'9' -> {
                    res = parseNumElemPair(torrent, index)
                    key = res.first
                    index += res.second
                }
                //TODO: continue with other types
            }
            when(torrent[index].toChar()) {
                in '0'..'9' -> {
                    res = parseNumElemPair(torrent, index)
                    value = res.first
                    index += res.second
                }
                //TODO: continue with other types
            }
            dict[key] = value
        }
        return dict
    }


    fun parse(torrent: ByteArray): HashMap<String, String>{ //TODO: fix return type as above
        when(torrent[0].toChar()){
            'd' -> return parseDict(torrent, 1)
        }
        return hashMapOf()
    }
}


//package app.shynline.torient.torrent.bencoding
//
//import app.shynline.torient.torrent.bencoding.common.BItem
//import app.shynline.torient.torrent.bencoding.common.Chars
//import app.shynline.torient.torrent.bencoding.common.InvalidBencodedException
//import java.io.BufferedInputStream
//import java.io.InputStream
//import java.util.*
//
//class BDict(bencoded: ByteArray? = null, item: LinkedHashMap<BString, BItem<*>>? = null) :
//    BItem<LinkedHashMap<BString, BItem<*>>>(bencoded, item) {
//
//    override fun encode(): ByteArray {
//        var res = "d".toByteArray()
//        value().forEach {
//            res += it.key.encode()
//            res += it.value.encode()
//        }
//        res += "e".toByteArray()
//        return res
//    }
//
//    override fun decode(bencoded: ByteArray): LinkedHashMap<BString, BItem<*>> {
//        var bc = bencoded.copyOf()
//        if (bc[0] != Chars.d)
//            throw InvalidBencodedException(
//                "BDict literals should start with d and end with e."
//            )
//        bc = bc.copyOfRange(1, bc.size)
//        val res: LinkedHashMap<BString, BItem<*>> = linkedMapOf()
//        var index: Int
//        var sub: ByteArray
//        var key: BString
//        var str: BString
//        while (bc[0] != Chars.e) {
//            if (!bc[0].toChar().isDigit())
//                throw InvalidBencodedException(
//                    "Invalid Bencoded Dict."
//                )
//            key = BString(bencoded = bc)
//            bc = bc.copyOfRange(key.encode().size, bc.size)
//            when (bc[0]) {
//                Chars.i -> {
//                    index = bc.indexOfFirst { it == Chars.e }
//                    if (index == -1)
//                        throw InvalidBencodedException(
//                            "Invalid Bencoded Dict."
//                        )
//                    sub = bc.toList().subList(0, index + 1).toByteArray()
//                    res[key] = BInteger(bencoded = sub)
//                    bc = bc.copyOfRange(sub.size, bc.size)
//                }
//                Chars.l -> {
//                    val bl = BList(bencoded = bc)
//                    res[key] = bl
//                    bc = bc.copyOfRange(bl.encode().size, bc.size)
//                }
//                Chars.d -> {
//                    val bd = BDict(bencoded = bc)
//                    res[key] = bd
//                    bc = bc.copyOfRange(bd.encode().size, bc.size)
//                }
//                else -> {
//                    if (!bc[0].toChar().isDigit())
//                        throw InvalidBencodedException(
//                            "Invalid Bencoded Dict."
//                        )
//                    str = BString(bencoded = bc)
//                    res[key] = str
//                    bc = bc.copyOfRange(str.encode().size, bc.size)
//                }
//            }
//        }
//        return res
//    }
//
//    override fun toString(short: Boolean, n: Int): String {
//        var mShort = short
//        val size = value().size
//        if (size < n + 1)
//            mShort = false
//        return buildString {
//            append("{")
//            value().toList().filterIndexed { index, pair ->
//                !mShort || index == size - 1 || index < n - 1
//            }.forEachIndexed { index, bItem ->
//                append(" ")
//                append(bItem.first.toString(mShort))
//                append(":")
//                append(bItem.second.toString(mShort))
//                if (!mShort) {
//                    if (index < size - 1)
//                        append(",")
//                } else {
//                    if (index < n - 1)
//                        append(",")
//                    if (index == n - 2) {
//                        append(" ... ")
//                        append(",")
//                    }
//                }
//            }
//            append(" }")
//        }
//    }
//
//    operator fun get(key: String): BItem<*>? {
//        return value()[BString(key)]
//    }
//
//    fun containsKey(key: String): Boolean {
//        return value().containsKey(BString(key))
//    }
//
//    companion object {
//        fun fromInputStream(inputStream: InputStream): BDict {
//            return BDict(bencoded = BufferedInputStream(inputStream).use { s ->
//                s.readBytes()
//            })
//        }
//    }
//}
