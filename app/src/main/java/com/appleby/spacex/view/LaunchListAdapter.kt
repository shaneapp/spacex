package com.appleby.spacex.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appleby.spacex.R
import com.appleby.spacex.networkmodel.Launch
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_launch.view.*

class LaunchListAdapter(private val context: Context) : RecyclerView.Adapter<LaunchListAdapter.ViewHolder>() {

    private val launchData = mutableListOf<Launch>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_launch, parent, false))
    }

    override fun getItemCount(): Int {
        return launchData.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launchItem = launchData[position]

        Glide.with(context)
            .load(launchItem.links.patch.small)
            .into(holder.ivBadge)

        holder.tvName.text = launchItem.name
        holder.tvDate.text = launchItem.date_utc
        holder.ivSuccess.setImageResource( if (launchItem.success) R.drawable.check else R.drawable.cancel )
    }

    fun updateData(data: List<Launch>) {
        launchData.clear()
        launchData.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivBadge = itemView.ivBadge
        var tvName = itemView.tvName
        var tvDate = itemView.tvDate
        var ivSuccess = itemView.ivSuccess
    }

}