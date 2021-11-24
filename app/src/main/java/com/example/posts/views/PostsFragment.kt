package com.example.posts.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts.R
import com.example.posts.adapter.MyAdapter
import com.example.posts.databinding.PostsFragmentBinding
import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import com.example.posts.viewmodels.PostFragmentEvent
import com.example.posts.viewmodels.PostFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment: Fragment() {

    private var _binding: PostsFragmentBinding? = null
    private val adapter: MyAdapter = MyAdapter()
    private val viewModel: PostFragmentViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initRecyclerView()

        viewModel.setStateEvent(PostFragmentEvent.GetPostEvents)

        //Refresh
        binding.swipeRefreshLayout.setOnRefreshListener{
            viewModel.setStateEvent(PostFragmentEvent.GetPostEvents)
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        viewModel.dataState.removeObservers(this)
        _binding = null
    }

    private fun initRecyclerView()
    {
        binding.mRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mRecyclerView.adapter = adapter
        binding.mRecyclerView.setHasFixedSize(false)
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, { datastate->
            when (datastate)
            {
                is DataStates.Success<List<Post>> ->
                {
                    if(datastate.data != null)
                        adapter.updatePosts(datastate.data)
                }
                is DataStates.Error -> {
                    displayError(datastate.exception.toString())

                }
            }
            binding.swipeRefreshLayout.isRefreshing = false

        })
    }


    //
    private fun displayError(message: String?)
    {
        message?.let{
            Toast.makeText(context, "Couldn't connect to host.Please check internet connection", Toast.LENGTH_SHORT).show()
       }
    }
}