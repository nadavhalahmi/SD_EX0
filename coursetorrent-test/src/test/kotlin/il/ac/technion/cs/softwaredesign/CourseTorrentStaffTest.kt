package il.ac.technion.cs.softwaredesign

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import il.ac.technion.cs.softwaredesign.storage.SecureStorageImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.*

class CourseTorrentStaffTest {
    private val torrent = CourseTorrent()

    private val debian1 = this::class.java.getResource("/debian-10.3.0-i386-netinst.iso.torrent").readBytes()
    private val debian2 = this::class.java.getResource("/debian-10.3.0-i386-netinst2.iso.torrent").readBytes()
    private val slack = this::class.java.getResource("/Slackware64_14.1.torrent").readBytes()  //

	init
    {
        SecureStorageImpl.clear()
    }
	
    @Nested
    inner class SmallTest
    {

        @Test
        fun `after load infohash calculated correctly`()
        {
            val infohash = torrent.load(debian1)
            assertWithTimeout {
                assertThat(infohash, equalTo("675b2b108626dda67ae532f8ddd4cbd821b2c943"))
            }
        }


        @Test
        fun `can't reload the same torrent twice`()
        {
            val infohash = torrent.load(debian1)
            assertWithTimeout{
                    assertThrows<IllegalStateException>{torrent.load(debian1)}
                }
        }

        @Test
        fun `loading 2 torrent files that have same hash and different announces`()
        {
            val infohash = torrent.load(debian1)
            assertWithTimeout{
                assertThrows<IllegalStateException>{torrent.load(debian2)}
            }
        }
        @Test
        fun `loading illegal torrent causes error`()
        {
            assertWithTimeout{
                assertThrows<IllegalArgumentException>{torrent.load("blabla".toByteArray()+debian1)}
            }
        }


        @Test
        fun `unloading a torrent succeeds`()
        {
            val infohash = torrent.load(slack)
            assertWithTimeout {
                assertDoesNotThrow { torrent.unload(infohash) }
            }
        }

        @Test
        fun `can load a torrent after unloading it`()
        {
            val infohash = torrent.load(slack)
            torrent.unload(infohash)
            assertWithTimeout {
                assertDoesNotThrow { torrent.load(slack) }
            }
        }

        //Test about normal announce, announce with unicode and big test
        @Test
        fun `announces for unloaded infohash`()
        {
            val infohash = torrent.load(slack)
            torrent.unload(infohash)
            assertWithTimeout {

                assertThrows<IllegalArgumentException> { torrent.announces(infohash) }
            }
        }

        @Test
        fun `announces for a single list`()
        {
            val infohash = torrent.load(debian1)

            assertWithTimeout {
                val announces = torrent.announces(infohash)

                assertThat(announces, allElements(hasSize(equalTo(1))))
                assertThat(announces, hasSize(equalTo(1)))
                assertThat(announces, allElements(hasElement("http://bttracker.debian.org:6969/announce")))

            }
        }

        @Test
        fun `announces for multiple lists with special unicode chars`()
        {
            val infohash = torrent.load(slack)

            assertWithTimeout {
                val announces = torrent.announces(infohash)

                assertThat(announces, hasSize(equalTo(2)))
                assertThat(announces.get(0), hasSize(equalTo(3)))
                assertThat(announces.get(1), hasElement("http://linuxtracker¿¡ñóéáúÑ!?.org:2710/00000000000000000000000000000000/announce"))
                assertThat(announces.get(1), hasElement("http://linuxtrackerגםבעברית.org:2710/00000000000000000000000000000000/announce"))

            }
        }
    }

    @Nested
    inner class BigTest
    {
        @Test
        fun `many torrents can be loaded to db`()
        {
            val idxToHash = loadTorrentsForBigTest(torrent)

            assertWithTimeout {
                assertThat(idxToHash[3257], equalTo("3f219eea64689ddfb217aba29f0acc0d1ec588f5"))
                assertThat(idxToHash[45184], equalTo("82ef6eb6b0232e68bdf9051444fd1f010256ad0b"))
                assertThat(idxToHash[654390], equalTo("c1aa9b0a498e7d9faadb9638073ff63b816abf72"))
            }
        }
    }


}