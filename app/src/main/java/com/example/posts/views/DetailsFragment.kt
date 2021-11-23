package com.example.posts.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.posts.R
import com.example.posts.databinding.DetailsFragmentBinding
import com.example.posts.retrofit.DataStates
import com.example.posts.viewmodels.DetailsFragmentViewModel
import com.example.posts.viewmodels.DetailsStateEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment(), DetailsFragmentViewModel.IDetailsFragment
{
    private var id: String = ""
    private var userId: String = ""
    private var title: String = ""
    private var body: String = ""

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private val TAG = "DetailsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {

        // Check if the bundle with the post details exists
        arguments?.let {
            safeLet(
                it.getString("id"),
                it.getString("userId"),
                it.getString("title"),
                it.getString("body")
            ) { bundleId ,bundleUserid, bundleTitle, bundleBody ->
                id = bundleId
                userId = bundleUserid
                title = bundleTitle
                body = bundleBody

            }
        }

        _binding = DetailsFragmentBinding.inflate(inflater, container, false)

        binding.detailsFab.setOnClickListener {
            viewModel.setStateEvent(this, DetailsStateEvent.Remove, id.toInt())
        }

        binding.detailsBackBtn.setOnClickListener{
            findNavController().navigate(R.id.action_DetailsFragment_to_PostFragment)
        }

        binding.postId.text = id
        binding.postTitle.text = title
        binding.postBody.text = body
        binding.postUserId.text = userId

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inline fun <T1: Any, T2: Any, T3: Any, T4:Any, R: Any> safeLet(p1: T1?, p2: T2?, p3:T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
        return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
    }

    // IDetailsFragment delegate functions
    override fun flowResponse(message: DataStates<Any>?)
    {
        if (message == null) return

        when(message){
            is DataStates.Success ->
            {
                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()

            }
            is DataStates.Error -> {
                Log.e(TAG, "flowResponse: ", message.exception)
            }
        }
        findNavController().navigate(R.id.action_DetailsFragment_to_PostFragment)
    }
}