package id.apwdevs.app.motvcatalogue.plugin

import android.content.res.Resources

class ExtensionPluginsImpl : ExtensionPlugins {
    override fun getResourceIdTypedArray(resources: Resources, resId: Int) : IntArray{
        val typedArray = resources.obtainTypedArray(resId)
        try {
            val arr = mutableListOf<Int>()
            for (i in 0 until typedArray.length()){
                arr.add(typedArray.getResourceId(i, -1))
            }
            return arr.toIntArray()
        } finally {
            typedArray.recycle()
        }
    }
}

interface ExtensionPlugins {
    fun getResourceIdTypedArray(resources: Resources, resId: Int) : IntArray
}