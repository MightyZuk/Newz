package com.example.newz

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewzAdapter(private val context: Context) : RecyclerView.Adapter<NewzAdapter.ViewHolder>(){

    private val arrayList : ArrayList<NewzData> = ArrayList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val newsImage : ImageView = view.findViewById(R.id.newsImage)
        val headline : TextView = view.findViewById(R.id.headline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_items,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNewz = arrayList[position]
        holder.headline.text = currentNewz.title
        Glide.with(this.context).load(currentNewz.urlToImage)
            .thumbnail(0.5f)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.newsImage)
        val url = currentNewz.url
        holder.itemView.setOnClickListener{
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this.context, Uri.parse(url))
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNewz(updateNewz : ArrayList<NewzData>){
        arrayList.clear()
        arrayList.addAll(updateNewz)

        notifyDataSetChanged()
    }
}