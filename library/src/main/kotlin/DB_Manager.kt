import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write
import java.nio.charset.Charset

class DB_Manager(private val charset: Charset = Charsets.UTF_8) {
    //var my_db : MutableMap<String, ByteArray> = HashMap<String, ByteArray>()

//    fun add(key: ByteArray, value: ByteArray){
//        write(key, value)
//    }

    fun add(key: String, value: ByteArray){
        val my_key = key.toByteArray(charset)
        write(my_key, value)
        //my_db[key] = value
    }

    fun get(key: String): ByteArray? {
        val res = read(key.toByteArray(charset))
        if(res == null || res.size==0)
            return null
        return res
        //return my_db
    }

    fun get(key: ByteArray): ByteArray? {
        val res = read(key)
        if(res == null || res.size==0)
            return null
        return res
    }

    fun delete(key: String): Unit {
        write(key.toByteArray(charset), ByteArray(0))
    }

    fun delete(key: ByteArray): Unit {
        write(key, ByteArray(0))
    }
}
