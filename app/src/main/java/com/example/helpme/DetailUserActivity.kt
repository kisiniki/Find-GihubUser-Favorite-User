package com.example.helpme

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.helpme.Adapter.SectionPagerAdapter
import com.example.helpme.Model.FavoriteModel
import com.example.helpme.Model.User
import com.example.helpme.ViewModel.DetailViewModel
import com.example.helpme.database.DatabaseContract
import com.example.helpme.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.helpme.database.UserHelper
import com.example.helpme.databinding.ActivityDetailUserBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var titlefollowers: String
    private var isFavorite = false
    private lateinit var userData: User
    private lateinit var favData: FavoriteModel
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        userData = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val cursor: Cursor = userHelper.querByIdl(userData.login)
        if (cursor.moveToNext()) {
            isFavorite = true
            favoriteCheck(true)
        }

        binding.fab.setOnClickListener(this)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(DetailViewModel::class.java)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        viewModel.setDetailUser(user.login)

        viewModel.getDetailUser().observe(this, Observer { detailUser ->
            if (detailUser != null) {
                binding.tvUsername.text = detailUser.login
                binding.tvName.text = detailUser.name
                Glide.with(binding.root.context)
                        .load(user.avatar_url)
                        .into(binding.imgUsers)
                binding.tvCompany.text = detailUser.company
                binding.tvLocation.text = detailUser.location
                binding.tvRepository.text = detailUser.public_repos.toString()
                binding.tvFollowers.text = getString(R.string.followers) + " " + detailUser.followers.toString()
                binding.tvFollowing.text = getString(R.string.following) + " " + detailUser.following.toString()
                titlefollowers = " " + detailUser.followers.toString()
            }
            showLoading(false)
        })
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                if (isFavorite) {
                    userHelper.deleteById(userData.login)
                    Snackbar.make(v, "berhasil hapus dari favorit", Snackbar.LENGTH_LONG)
                            .setAction("action", null)
                            .show()
                    favoriteCheck(false)
                } else {
                    val values = ContentValues()
                    values.put(DatabaseContract.UserColumns.LOGIN, userData.login)
                    values.put(DatabaseContract.UserColumns.AVATAR_URL, userData.avatar_url)
                    values.put(DatabaseContract.UserColumns.FAVORITE, "1")
                    isFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                    favoriteCheck(true)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

    private fun favoriteCheck(status: Boolean) {
        if (status) {
            binding.fab.setImageResource(R.drawable.ic_full_baseline_favorite_24)
        } else {
            binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}