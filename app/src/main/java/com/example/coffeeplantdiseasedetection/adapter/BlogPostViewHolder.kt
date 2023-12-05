package com.example.coffeeplantdiseasedetection.adapter

import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeplantdiseasedetection.R

class BlogPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    val webView: WebView = itemView.findViewById(R.id.webView)
}