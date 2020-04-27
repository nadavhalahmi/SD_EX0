# CourseApp: Assignment 0

## Authors
* Nadav Halahmi, 206784258

## Notes

### Implementation Summary
Lets take an example of a torrent, with info hash "123" and keys in the main dictionary: info, announce.
We have a key-value database accessed through read/write calls, in which we keep for each torrent:
hashinfo -> torrent as bytearray (Ex: 123 -> torrent as bytearray)
hashinfo^info -> torrent info value as bytearray (Ex: 123info -> the value of "info" key in torrent as bytearray)
same as above for announce.
Then it's easy to get the whole torrent, or parts of it. If you want, you can then parse a single part of the torrent,
for example parsse the announce-list, and easily get a list of list of strings, with the announce list.
In order to do so we used two main classes:
Parser - in order to parse the torrent and it's parts
DB_Manager - in order to manage access to the database (in the future, this class might change a bit
in order to store things that arent torrents)
Both classes use some other classes:
TorrentElement - could be TorrentDict, TorrentList, Int or String. It also keeps a range in the original torrent,
so it's easy to know where this element is located in the original torrent.
TorrentDict - reperesents a dictionary of string -> TorrentElement
TorrentList - reperesents a list of TorrentElement

### Testing Summary
COMPLETE.

### Difficulties
Mockk and Gradle where technically hard to use. 
Changed to this line in gradle-wrapper.properties: distributionUrl=https\://services.gradle.org/distributions/gradle-6.3-all.zip

### Feedback
It email feedback was good, but it would be better if we wont need to use it.
It wasnt very clear where we should write code.
