package com.example.posts.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.R
import com.example.posts.adapter.MyAdapter
import com.example.posts.databinding.PostsFragmentBinding
import com.example.posts.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PostsFragment: Fragment() {

    private var _binding: PostsFragmentBinding? = null
    private val adapter: MyAdapter = MyAdapter()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PostsFragmentBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_PostsFragment_to_CreatePostFragment)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView()
    {
        binding.mRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mRecyclerView.adapter = adapter
        binding.mRecyclerView.setHasFixedSize(false)

        val dummyDataList:MutableList<Post> = mutableListOf(Post(1,11,"test title1", "test body1"),
            Post(2,22,"test title2", "test body2"))
        adapter.updatePosts(dummyDataList)
        adapter.notifyDataSetChanged()
    }
}