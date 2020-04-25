package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.storage.write
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;




class CourseTorrentStaffTest {
    private val torrent = CourseTorrent()
    //val myCT = mockk<CourseTorrent>()
    //private val debian = this::class.java.getResource("/my.torrent").readBytes()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    @Test
    fun `after load, infohash calculated correctly`() {
        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(any(), any()) } throws Exception()
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