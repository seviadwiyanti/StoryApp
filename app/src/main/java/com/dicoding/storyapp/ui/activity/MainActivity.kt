package com.dicoding.storyapp.ui.activity

import MainViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.adapter.ListStoryAdapter
import com.dicoding.storyapp.data.adapter.LoadingStateAdapter
import com.dicoding.storyapp.data.paging.entity.StoryEntity
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.viewmodel.ViewModelFactory

//class MainActivity : AppCompatActivity() {
//    private val viewModel by viewModels<MainViewModel> {
//        ViewModelFactory.getInstance(this)
//    }
//    private lateinit var binding: ActivityMainBinding
//
//    private val mAdapter = ListStoryAdapter(this)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.toolbar.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.action_logout -> {
//                    AlertDialog.Builder(this).apply {
//                        setTitle(getString(R.string.logout_title))
//                        setMessage(getString(R.string.logout_message))
//                        setPositiveButton(getString(R.string.yes_title)) { _, _ ->
//                            viewModel.logout()
//                            showToast(getString(R.string.logout_success))
//                        }
//                        setNegativeButton(getString(R.string.no_title)) { _, _ ->
//                        }
//                        create()
//                        show()
//                    }
//                    true
//                }
//
//                R.id.action_setting -> {
//                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//                    true
//                }
//
//                R.id.action_maps -> {
//                    startActivity(Intent(this, MapsActivity::class.java))
//                    true
//                }
//
//                else -> false
//            }
//        }
//
//        binding.fabAdd.setOnClickListener {
//            startActivity(
//                Intent(
//                    this,
//                    TambahStoryActivity::class.java
//                )
//            )
//        }
//
////        viewModel.getSession().observe(this) { user ->
////            if (!user.isLogin) {
////                startActivity(Intent(this, WelcomeActivity::class.java))
////                finish()
////            } else {
////                viewModel.getStori().observe(this) { result ->
////                    if (result != null) {
////                        when (result) {
////                            is ResultValue.Loading -> {
////                                showLoading(true)
////                            }
////
////                            is ResultValue.Success -> {
////                                showViewModel(result.data.listStory)
////                                showLoading(false)
////                            }
////
////                            is ResultValue.Error -> {
////                                showToast(result.error)
////                                showLoading(false)
////                            }
////                        }
////                    } else {
////                        showToast(getString(R.string.empty_story))
////                    }
////                }
////            }
////        }
//        showRecycleView()
//    }
//
//    private fun showRecycleView() {
//        val mLayoutManager = LinearLayoutManager(this)
//        binding.rvStories.apply {
//            layoutManager = mLayoutManager
//            setHasFixedSize(true)
//            adapter = mAdapter
//        }
//
//        mAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ListStoryItem) {
//                showSelectedUser(data)
//            }
//        })
//    }
//
//    private fun getData() {
//        mAdapter.withLoadStateFooter(
//            footer = LoadingStateAdapter { mAdapter.retry() }
//        )
//        viewModel.stories.observe(this) { data ->
//            if (data != null) {
//                mAdapter.submitData(lifecycle, data)
//                showLoading(false)
//            } else {
//                showLoading(true)
//                showToast(getString(R.string.empty_story))
//            }
//        }
//    }
//
//    private fun showViewModel(storiesItem: List<ListStoryItem>) {
//        if (storiesItem.isNotEmpty()) {
//            binding.rvStories.visibility = View.VISIBLE
//            mAdapter.submitList(storiesItem)
//        } else {
//            binding.rvStories.visibility = View.INVISIBLE
//        }
//    }
//
//    private fun showSelectedUser(stories: ListStoryItem) {
//        val intentToDetail = Intent(this@MainActivity, StoryDetailActivity::class.java)
//        intentToDetail.putExtra("STORY_ID", stories.id)
//        startActivity(intentToDetail)
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//}

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private val mAdapter = ListStoryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.logout_title))
                        setMessage(getString(R.string.logout_message))
                        setPositiveButton(getString(R.string.yes_title)) { _, _ ->
                            viewModel.logout()
                            showToast(getString(R.string.logout_success))
                        }
                        setNegativeButton(getString(R.string.no_title)) { _, _ ->
                        }
                        create()
                        show()
                    }
                    true
                }

                R.id.action_setting -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.action_maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }

        binding.fabAdd.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TambahStoryActivity::class.java
                )
            )
        }
        checkUserLoginAndRedirect()
        showRecycleView()
        getData()  // Memanggil getData untuk mulai memuat data
    }

    private fun checkUserLoginAndRedirect() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                getData()
            }
        }
    }

    private fun showRecycleView() {
        val mLayoutManager = LinearLayoutManager(this)
        binding.rvStories.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { mAdapter.retry() }
            )
        }

        mAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryEntity) {
                showSelectedUser(data)
            }
        })
    }



//    private fun showRecycleView() {
//        val mLayoutManager = LinearLayoutManager(this)
//        binding.rvStories.apply {
//            layoutManager = mLayoutManager
//            setHasFixedSize(true)
//            adapter = mAdapter.withLoadStateFooter(
//                footer = LoadingStateAdapter { mAdapter.retry() }
//            )
//        }
//
//        mAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: StoryEntity) {  // Menggunakan StoryEntity
//                showSelectedUser(data)
//            }
//        })
//    }

//    private fun getData() {
//        viewModel.stories.observe(this) { data ->
//            if (data != null) {
//                mAdapter.submitData(lifecycle, data)
//                showLoading(false)
//            } else {
//                showLoading(true)
//                showToast(getString(R.string.empty_story))
//            }
//        }
//    }

    private fun getData() {
        viewModel.stories.observe(this) { data ->
            data?.let { nonNullData ->
                mAdapter.submitData(lifecycle, nonNullData)
                showLoading(false)
            } ?: run {
                showLoading(true)
                showToast(getString(R.string.empty_story))
            }
        }
    }


    private fun showSelectedUser(story: StoryEntity) {  // Menggunakan StoryEntity
        val intentToDetail = Intent(this@MainActivity, StoryDetailActivity::class.java)
        intentToDetail.putExtra("STORY_ID", story.id)
        startActivity(intentToDetail)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
