class TorrentString(value: String, startIndex: Int, endIndex: Int): TorrentElement(value, startIndex, endIndex) {

    override fun toString(): String{
        return value as String
    }



//    fun Equals(other: Any?): Boolean { //TODO: change to equals
//        if(other !is Map<*, *>)
//            return false
//        for(key in dict.keys){
//            if(!other.containsKey(key))
//                return false
//            val value = dict[key]
//            val toCompareVal = other[key]
//            if (value != null) {
//                if(value != toCompareVal)
//                    return false
//            }
//        }
//        return true
//    }
}