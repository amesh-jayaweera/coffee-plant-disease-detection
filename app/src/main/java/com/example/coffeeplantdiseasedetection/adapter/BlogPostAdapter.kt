package com.example.coffeeplantdiseasedetection.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeplantdiseasedetection.R
import com.example.coffeeplantdiseasedetection.model.BlogItem

class BlogPostAdapter(private val blogPosts: List<BlogItem>) : RecyclerView.Adapter<BlogPostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogPostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.blog_item_layout, parent, false)
        return BlogPostViewHolder(view)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: BlogPostViewHolder, position: Int) {
        val blogPost = blogPosts[position]
        holder.titleTextView.text = blogPost.title
        holder.descriptionTextView.text = blogPost.description
        holder.webView.settings.javaScriptEnabled = true
        holder.webView.loadUrl(blogPost.url)

        holder.itemView.setOnClickListener {
            // Open the blog post in a web browser
            val uri = Uri.parse(blogPost.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return blogPosts.size
    }
}