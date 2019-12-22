package id.apwdevs.app.motvcatalogue.adapter

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.model.DataModel

class RecyclerContentAdapter(
    internal val mData: List<DataModel>,
    internal val mPosterSize: Point
) : RecyclerView.Adapter<RecyclerContentAdapter.ContentAdapterVH>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentAdapterVH =
        ContentAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_content_item, parent, false)
        )

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ContentAdapterVH, position: Int) {
        holder.bind(mData[position])
    }

    inner class ContentAdapterVH(view: View) : RecyclerView.ViewHolder(view){
        private val mTitleText : TextView = view.findViewById(R.id.item_list_text_title)
        private val mReleaseText : TextView = view.findViewById(R.id.item_list_release_date)
        private val mOverview : TextView = view.findViewById(R.id.item_list_overview)
        private val mImagePoster : ImageView = view.findViewById(R.id.item_list_image)

        internal fun bind(data: DataModel){
            data.apply {
                mTitleText.text = title
                mReleaseText.text = releaseDate
                mOverview.text = overview
                Glide.with(itemView.context).load(photoRes).apply(RequestOptions().override(mPosterSize.x, mPosterSize.y)).into(mImagePoster)
            }
        }
    }

}