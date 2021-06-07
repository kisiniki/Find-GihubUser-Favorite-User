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
import com.example.helpme.ViewModel.FollowersViewModel
import com.example.helpme.databinding.FragmentFollowBinding

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private lateinit var adapter: MainAdapter
    private lateinit var followersVM: FollowersViewModel

    private val binding get() = _binding!!

    companion object{
        const val EXTRA_USER = "extra_user"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFollowBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            showRecyclerView()
            getFollowersViewModel()
    }

    private fun getFollowersViewModel(){
        followersVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java)
        val query = activity?.intent?.getParcelableExtra<User>(EXTRA_USER)
        showLoading(true)
        followersVM.setFollowers(query!!.login)
        followersVM.getFollowers().observe(viewLifecycleOwner, Observer { followersData ->
            if (followersData != null){
                adapter.setlist(followersData)
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

    private fun showLoading(state: Boolean){
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}