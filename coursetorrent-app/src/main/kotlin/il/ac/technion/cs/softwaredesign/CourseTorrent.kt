package il.ac.technion.cs.softwaredesign

import DB_Manager
import TorrentDict
import TorrentList
import TorrentParser
import kotlin.IllegalArgumentException as IllegalArgumentException1

/**
 * This is the class implementing CourseTorrent, a BitTorrent client.
 *
 * Currently specified:
 * + Parsing torrent metainfo files (".torrent" files)
 */
class CourseTorrent {
    private val dbManager = DB_Manager()
    private val parser = TorrentParser()
    /**
     * Load in the torrent metainfo file from [torrent]. The specification for these files can be found here:
     * [Metainfo File Structure](https://wiki.theory.org/index.php/BitTorrentSpecification#Metainfo_File_Structure).
     *
     * After loading a torrent, it will be available in the system, and queries on it will succeed.
     *
     * This is a *create* command.
     *
     * @throws IllegalArgumentException1 If [torrent] is not a valid metainfo file.
     * @throws IllegalStateException If the infohash of [torrent] is already loaded.
     * @return The infohash of the torrent, i.e., the SHA-1 of the `info` key of [torrent].
     */
    fun load(torrent: ByteArray): String {
        val infoValue: ByteArray
        val dict: TorrentDict
        try {
            //infoValue = parser.getValueByKey(torrent, "info")
            dict = parser.parse(torrent)
            val infoRange = dict.getRange("info")
            infoValue = torrent.copyOfRange(infoRange.startIndex(), infoRange.endIndex())
        }catch (e: Exception){
            throw IllegalArgumentException1()
        }
        val infohash = parser.SHAsum(infoValue)
        if(dbManager.exists(infohash))
            throw IllegalStateException()
        dbManager.add(infohash, torrent, dict)
        return infohash
    }

    /**
     * Remove the torrent identified by [infohash] from the system.
     *
     * This is a *delete* command.
     *
     * @throws IllegalArgumentException1 If [infohash] is not loaded.
     */
    fun unload(infohash: String): Unit {
        if(!dbManager.exists(infohash))
            throw IllegalArgumentException1()
        dbManager.delete(infohash)
    }

    /**
     * Return the announce URLs for the loaded torrent identified by [infohash].
     *
     * See [BEP 12](http://bittorrent.org/beps/bep_0012.html) for more information. This method behaves as follows:
     * * If the "announce-list" key exists, it will be used as the source for announce URLs.
     * * If "announce-list" does not exist, "announce" will be used, and the URL it contains will be in tier 1.
     * * The announce URLs should *not* be shuffled.
     *
     * This is a *read* command.
     *
     * @throws IllegalArgumentException1 If [infohash] is not loaded.
     * @return Tier lists of announce URLs.
     */
    fun announces(infohash: String): List<List<String>> {
        var lst: ByteArray? = dbManager.get(infohash, "announce-list")
        if(lst === null) {
            lst = dbManager.get(infohash, "announce")
            if (lst === null)
                throw IllegalArgumentException1()
            else
                lst = "ll".toByteArray(Charsets.UTF_8) + lst + "ee".toByteArray(Charsets.UTF_8)
        }
        //return listOf((parser.parseList(lst).value() as TorrentList).toList() as List<String>)
        return ((parser.parseList(lst).value() as TorrentList).toList() as List<List<String>>)
    }
}