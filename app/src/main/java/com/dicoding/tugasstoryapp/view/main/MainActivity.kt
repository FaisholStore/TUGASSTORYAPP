package com.dicoding.tugasstoryapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasstoryapp.R
import com.dicoding.tugasstoryapp.Response.StoryItem
import com.dicoding.tugasstoryapp.data.Models.UserPref
import com.dicoding.tugasstoryapp.data.Models.dataStore
import com.dicoding.tugasstoryapp.databinding.ActivityMainBinding
import com.dicoding.tugasstoryapp.view.Adapter.StoryAdapter
import com.dicoding.tugasstoryapp.view.ViewModelFactory
import com.dicoding.tugasstoryapp.view.login.LoginActivity
import com.dicoding.tugasstoryapp.view.story.AddStoryActivity

class MainActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(UserPref.getInstance(dataStore))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        binding.root.setOnRefreshListener {
            setupView()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_story -> AddStoryActivity.start(this)
            R.id.logout -> showLogoutDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("Anda yakin ingin Logout?")
            setPositiveButton("Ya") { _, _ ->
                logout()
            }
            setNegativeButton("Tidak", null)
            create()
            show()
        }
    }

    private fun logout() {
        mainViewModel.logout()
        LoginActivity.start(this)
        finish()
    }

    private fun setRecycleView(storyItems: List<StoryItem>) {
        with(binding) {
            val manager = LinearLayoutManager(this@MainActivity)
            rvStory.apply {
                adapter = StoryAdapter(storyItems)
                layoutManager = manager
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel.listStory.observe(this) {
            setRecycleView(it)
        }
        mainViewModel.loadingScreen.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(value: Boolean) {
        binding.pbLoadingScreen.isVisible = value
        binding.rvStory.isVisible = !value
    }

    private fun setupView() {
        binding.root.isRefreshing = false
        mainViewModel.getStories()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            starter.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(starter)
        }
    }
}