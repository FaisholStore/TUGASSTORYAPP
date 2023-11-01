package com.dicoding.tugasstoryapp.view.Detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dicoding.tugasstoryapp.Response.StoryItem
import com.dicoding.tugasstoryapp.databinding.ActivityDetailStoryBinding
import com.google.android.material.snackbar.Snackbar

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding


    private val userId by lazy { intent.getStringExtra(USER_ID)
    }
    private  val detailView by viewModels<DetailView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        SetView()
    }

    override fun onResume() {
        super.onResume()
        detailView.getDetailStory(userId as String)
    }

    private fun setupViewModel() {
        detailView.detailStory.observe(this) {
            setDetailStory(it)
        }

        detailView.loadingScreen.observe(this) {
            showLoading(it)
        }

        detailView.snackBarText.observe(this) {
            it.getContentIfNotHandled().let { text ->
                Snackbar.make(binding.root, text.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDetailStory(storyItem: StoryItem) {
        with(binding) {
            tvUserName.text = storyItem.name
            tvUserDesc.text = storyItem.description
        }
    }

    private fun showLoading(value: Boolean) {
        with(binding) {
            progressbardetailstory.isVisible = value
            tvUserName.isVisible = !value
            tvUserDesc.isVisible = !value
        }
    }

    private fun SetView() {
        Glide.with(this@DetailStoryActivity)
            .load(intent.getStringExtra(PHOTO_URL))
            .into(binding.ivUserPhoto)
        detailView.getDetailStory(userId as String)
    }

    companion object {
        @JvmStatic
        fun start(context: Context, photoUrl: String, userId: String, pair:Pair<View, String>) {
            val starter = Intent(context, DetailStoryActivity::class.java)
                .putExtra(USER_ID, userId)
                .putExtra(PHOTO_URL, photoUrl)

            val optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,pair)
            context.startActivity(starter, optionsCompat.toBundle())
        }

        private const val USER_ID = "userId"
        private const val PHOTO_URL = "photo_url"
    }
}