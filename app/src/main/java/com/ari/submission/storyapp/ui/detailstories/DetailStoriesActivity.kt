package com.ari.submission.storyapp.ui.detailstories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ari.submission.storyapp.R
import com.ari.submission.storyapp.databinding.ActivityDetailStoriesBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_stories.*

class DetailStoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = intent.getStringExtra("list_name")
        binding.ivDetailName.text = intent.getStringExtra("list_name")
        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .error(R.drawable.ic_launcher_background)
            .into(iv_detail_photo)
        binding.ivDetailDescription.text = intent.getStringExtra("list_description")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}