package com.example.posts.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.posts.R
import com.example.posts.databinding.CreatePostFragmentBinding
import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import com.example.posts.viewmodels.CreatePostEvent
import com.example.posts.viewmodels.CreatePostFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreatePostFragment : Fragment(), CreatePostFragmentViewModel.ICreatePost{

    private val TAG = "CreatePostFragment"
    private val viewModel: CreatePostFragmentViewModel by viewModels()
    private var _binding: CreatePostFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createPost.setOnClickListener{
            val id: String = binding.createPostInputId.text.toString()
            val userId = binding.createPostInputUserId.text.toString()
            val title = binding.createPostInputTitle.text.toString()
            val body = binding.createPostInputBody.text.toString()

            if (id.isBlank() || userId.isBlank()
                || title.isBlank() || body.isBlank()){
                Toast.makeText(context, "Please fill all fields. ", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val post = Post(id.toInt(), userId.toInt(), title, body)
                viewModel.setStateEvent(this, CreatePostEvent.CreatePost, post)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun sendResponse(datastate: DataStates<Any>?)
    {
        if (datastate == null) return

        when(datastate){
            is DataStates.Success ->
            {
                Toast.makeText(context, "Post Created successfully", Toast.LENGTH_SHORT).show()
            }
            is DataStates.Error -> {

                Log.e(TAG, "Error sendResponse: " +  datastate.exception.toString() )

            }
        }
        findNavController().navigate(R.id.action_CreatePostFragment_to_PostsFragment)
    }
}