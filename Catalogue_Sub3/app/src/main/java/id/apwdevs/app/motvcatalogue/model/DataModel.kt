package id.apwdevs.app.motvcatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel (

    val photoRes: Int,
    var overview: CharSequence?,
    var releaseDate: CharSequence?,
    var title: CharSequence?,
    var isFavorite: Boolean = false
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataModel

        if (photoRes != other.photoRes) return false
        if (overview != other.overview) return false
        if (releaseDate != other.releaseDate) return false
        if (title != other.title) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photoRes
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}