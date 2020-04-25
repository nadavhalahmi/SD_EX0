import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write
import java.nio.charset.Charset

class DB_Manager(private val charset: Charset = Charsets.UTF_8) {
    //var my_db : MutableMap<String, ByteArray> = HashMap<String, ByteArray>()

//    fun add(key: ByteArray, value: ByteArray){
//        write(key, value)
//    }

    fun add(hash: String, value: ByteArray, dict: TorrentDict){
        val hashBytes = hash.toByteArray(charset)
        write(hashBytes, value)
        for(key in dict.keys) {
            val range = dict.getRange(key)
            write((hash + key).toByteArray(), value.copyOfRange(range.startIndex(), range.endIndex()))
        }
    }

    fun get(hash: String, key: String = ""): ByteArray? {
        val res = read((hash).toByteArray(charset))
        if(res == null || res.size==0)
            return null
        return read((hash+key).toByteArray(charset))
    }

    fun get(hash: ByteArray, key: String = ""): ByteArray? {
        val res = read(hash)
        if(res == null || res.size==0)
            return null
        return read(hash+key.toByteArray(charset))
    }

    fun delete(key: String): Unit {
        write(key.toByteArray(charset), ByteArray(0))
    }

    fun delete(key: ByteArray): Unit {
        write(key, ByteArray(0))
    }
}
