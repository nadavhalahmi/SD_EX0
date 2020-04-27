package il.ac.technion.cs.softwaredesign

import DB_Manager
import MyByteArray
import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.storage.read
import il.ac.technion.cs.softwaredesign.storage.write

import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot


class CourseTorrentTest {
    private val charset = Charsets.UTF_8
    private val torrent = CourseTorrent()
    private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val my_db = HashMap<MyByteArray, MyByteArray>()
    @Test
    fun `after load, infohash calculated correctly`() {
        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        val keySlot = slot<ByteArray>()
        val valueSlot = slot<ByteArray>()
        every { write(key=capture(keySlot), value = capture(valueSlot)) } answers
                {my_db[MyByteArray(keySlot.captured)] = MyByteArray(valueSlot.captured)}
        val infohash = torrent.load(debian)

        assertThat(infohash, equalTo("5a8062c076fa85e8056451c0d9aa04349ae27909"))
    }

    @Test
    fun `after load, announce is correct`() {
        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        val keySlot = slot<ByteArray>()
        val valueSlot = slot<ByteArray>()
        every { write(key=capture(keySlot), value = capture(valueSlot)) } answers
                {my_db[MyByteArray(keySlot.captured)] = MyByteArray(valueSlot.captured)
                    println("writing key "+keySlot.captured.toString(charset))}
        every { read(key=capture(keySlot)) } answers {
            println("reading key "+keySlot.captured.toString(charset))
            my_db[MyByteArray(keySlot.captured)]?.arr  }
        val infohash = torrent.load(debian)

        val announces = torrent.announces(infohash)

        assertThat(announces, allElements(hasSize(equalTo(1))))
        assertThat(announces, hasSize(equalTo(1)))
        assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))
    }


}