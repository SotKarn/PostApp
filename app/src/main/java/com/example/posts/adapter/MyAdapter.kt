package com.example.posts.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.posts.R
import com.example.posts.model.Post

open class MyAdapter: Adapter<MyAdapter.ViewHolder>()
{
    private var postList: MutableList<Post>? = null
    class ViewHolder(itemView: View, postList: MutableList<Post>?) : RecyclerView.ViewHolder(itemView)
    {
        val id: TextView = itemView.findViewById(R.id.list_item_id)
        val title: TextView = itemView.findViewById(R.id.list_item_title)

        init
        {
            itemView.setOnClickListener {

                postList?.let { list ->
                    val bundle = Bundle()
                    bundle.putString("id", id.text as String)
                    bundle.putString("title", title.text as String)
                    val body = list.find { post -> post.id.toString() == id.text }?.body
                    val userId = list.find { post -> post.id.toString() == id.text }?.userId.toString()
                    bundle.putString("body", body)
                    bundle.putString("userId", userId)
                    it.findNavController().navigate(R.id.action_PostsFragment_to_DetailsFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
       return ViewHolder(view, postList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.id.text = postList?.get(position)?.id.toString()
        holder.title.text = postList?.get(position)?.title
    }

    override fun getItemCount(): Int
    {
        postList?.let{
            return it.size
        } ?: return 0
    }

    fun updatePosts(posts: List<Post>)
    {
        postList?.let {
            it.clear()
            it.addAll(posts)
        }?: run {
            postList = mutableListOf()
            postList!!.addAll(posts)
        }
    }
}