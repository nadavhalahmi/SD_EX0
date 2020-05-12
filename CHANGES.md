Changes:
CourseTorrent.kt:
line 42:		REPLACE if(dbManager.get(infohash) !== null)
				BY if(dbManager.exists(infohash))
	
line 56:		REPLACE if(dbManager.get(infohash) === null)
				BY if(!dbManager.exists(infohash))
	
line 81:		REPLACE lst = "l".toByteArray(Charsets.UTF_8) + lst + "e".toByteArray(Charsets.UTF_8)
				BY lst = "ll".toByteArray(Charsets.UTF_8) + lst + "ee".toByteArray(Charsets.UTF_8)
	
line 83:		REPLACE return listOf((parser.parseList(lst).value() as TorrentList).toList() as List<String>)
				BY return ((parser.parseList(lst).value() as TorrentList).toList() as List<List<String>>)
			
library/src/main/kotlin/DB_Manager.kt:
after line 19:	ADD write((hash+"exists").toByteArray(charset), "true".toByteArray(charset))

after line 25: 	ADD fun exists(hash: String, key: String = ""): Boolean {
						return exists((hash).toByteArray(charset),key)
					}

					fun exists(hash: ByteArray, key: String = ""): Boolean {
						return read(hash + "exists".toByteArray(charset))?.isNotEmpty() ?: false
					}

lines 34-36:	REPLACE val res = read(hash)
						if(res === null || res.size==0)
							return null
				BY if(!exists(hash)) return null

line 49:		REPLACE write(key, ByteArray(0))
				BY write(key+"exists".toByteArray(charset), ByteArray(0))
				
library/src/main/kotlin/Parser.kt:
line 76:		REPLACE res = parseBytes(torrent, elemStartIndex) { it-elemStartIndex >= elemLen } //parse key
				BY res = torrent.copyOfRange(elemStartIndex, elemStartIndex+elemLen).toString(Charsets.UTF_8)

line 78:		REPLACE: pairLen += res.length
				BY: pairLen += elemLen

line 139: 		REPLACE return TorrentElement(res.toInt(), startIndex, startIndex+res.length+2) //+2 for i and e
				BY return TorrentElement(res.toBigInteger(), startIndex, startIndex+res.length+2) //+2 for i and e
