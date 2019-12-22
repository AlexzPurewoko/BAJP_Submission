package id.apwdevs.app.motvcatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.plugin.WrappedView

class RecyclerContentAboutAdapter(
    private val data: HashMap<String, String>
) : RecyclerView.Adapter<RecyclerContentAboutAdapter.RecyclerAboutVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAboutVH =
        RecyclerAboutVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_about, parent, false)
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerAboutVH, position: Int) {
        val key = data.keys.toList()[position]
        holder.bind(key, data)
    }

    class RecyclerAboutVH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.item_title)
        private val wrappedView: WrappedView = view.findViewById(R.id.item_content)

        fun bind(key: String, dataReference: HashMap<String, String>){
            title.text = key
            if (wrappedView.childCount ==0){
                dataReference[key]?.apply {
                    if(startsWith("$")){
                        wrappedView.addText(this)
                    } else {
                        split(",").forEach {
                            wrappedView.addText(it)
                        }
                    }
                }
            }

        }
    }
}