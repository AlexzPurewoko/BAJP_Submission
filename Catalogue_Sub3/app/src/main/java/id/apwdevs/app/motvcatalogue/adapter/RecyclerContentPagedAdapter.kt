package id.apwdevs.app.motvcatalogue.adapter

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity

class RecyclerContentPagedAdapter(
    internal val mPosterSize: Point
) : PagedListAdapter<DataModelEntity, RecyclerContentPagedAdapter.ContentAdapterVH>(DIFF_CALLBACK){

    companion object {
        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataModelEntity>() {
            override fun areItemsTheSame(oldItem: DataModelEntity, newItem: DataModelEntity): Boolean =
                oldItem.photoRes == newItem.photoRes

            override fun areContentsTheSame(oldItem: DataModelEntity, newItem: DataModelEntity): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentAdapterVH =
        ContentAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_content_item, parent, false)
        )

    override fun onBindViewHolder(holder: ContentAdapterVH, position: Int) {
        getItem(position)?.apply { holder.bind(this) }

    }

    fun getData(position: Int) : DataModelEntity? = getItem(position)

    inner class ContentAdapterVH(view: View) : RecyclerView.ViewHolder(view){
        private val mTitleText : TextView = view.findViewById(R.id.item_list_text_title)
        private val mReleaseText : TextView = view.findViewById(R.id.item_list_release_date)
        private val mOverview : TextView = view.findViewById(R.id.item_list_overview)
        private val mImagePoster : ImageView = view.findViewById(R.id.item_list_image)

        internal fun bind(data: DataModelEntity){
            data.apply {
                mTitleText.text = titleItem
                mReleaseText.text = releaseDate
                mOverview.text = overview
                Glide.with(itemView.context).load(photoRes).apply(RequestOptions().override(mPosterSize.x, mPosterSize.y)).into(mImagePoster)
            }
        }
    }

}