package id.apwdevs.app.motvcatalogue.plugin

object AnotherAboutIntoHashMap {
    fun intoHashMap(sParser: String, keys: List<String>) : HashMap<String, String> {
        val allComponents = sParser.split("[|]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hashMap = hashMapOf<String, String>()
        for ((index, value) in keys.withIndex()){
            hashMap[value] = allComponents[index]
        }
        return hashMap
    }
}