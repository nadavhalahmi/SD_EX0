package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

fun SHAsum(convertme: ByteArray) : String{
    var md = MessageDigest.getInstance("SHA-1");
    return byteArray2Hex(md.digest(convertme));
}

fun byteArray2Hex(hash: ByteArray) : String{
    var formatter = Formatter();
    for (b in hash) {
        formatter.format("%02x", b);
    }
    return formatter.toString();
}


class CourseTorrentStaffTest {
    private val torrent = CourseTorrent()
    //private val debian = this::class.java.getResource("/my.torrent").readBytes()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    @Test
    fun `after load, infohash calculated correctly`() {
        val infohash = torrent.load(debian)

        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }

    @Test
    fun `after load, announce is correct`() {
        val infohash = torrent.load(debian)

        val announces = torrent.announces(infohash)

        assertThat(announces, allElements(hasSize(equalTo(1))))
        assertThat(announces, hasSize(equalTo(1)))
        assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }
}