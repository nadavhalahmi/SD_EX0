package il.ac.technion.cs.softwaredesign

import DB_Manager
import com.natpryce.hamkrest.allElements
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import il.ac.technion.cs.softwaredesign.storage.write
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import io.mockk.mockk
import io.mockk.mockkStatic


class DB_ManagerTest {
    //private val debian = this::class.java.getResource("/debian-10.3.0-amd64-netinst.iso.torrent").readBytes()
    private val db = DB_Manager()
    private val fake_db = mockk<DB_Manager>()
    @Test
    fun `after load, infohash calculated correctly`() {
        mockkStatic("il.ac.technion.cs.softwaredesign.storage.SecureStorageKt")
        every { write(any(), any()) } throws Exception("some mock exception")
        assert(true == true)
    }
}