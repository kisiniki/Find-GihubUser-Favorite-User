package com.example.helpme.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpme.Adapter.MainAdapter
import com.example.helpme.Model.User
import com.example.helpme.ViewModel.FollowingViewModel
import com.example.helpme.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: MainAdapter
    private val binding get() = _binding!!

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerView()
        getFollowingViewModel()
    }

    private fun getFollowingViewModel(){
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        val query = activity?.intent?.getParcelableExtra<User>(EXTRA_USER)
        showLoading(true)
        viewModel.setFollowing(query!!.login)
        viewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingData ->
            if (followingData != null){
                adapter.setlist(followingData)
                showLoading(false)
            }
        })
    }
    private fun showRecyclerView(){
        adapter = MainAdapter()
        binding.rvFollow.layoutManager = LinearLayoutManager(context)
        binding.rvFollow.adapter = adapter
        binding.rvFollow.setHasFixedSize(true)
    }
    fun showLoading(state: Boolean){
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}