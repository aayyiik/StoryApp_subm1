package com.ari.submission.storyapp.ui.home

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ari.submission.storyapp.R
import com.ari.submission.storyapp.background.ApiConfig
import com.ari.submission.storyapp.background.GetAllStoryResponse
import com.ari.submission.storyapp.data.Stories
import com.ari.submission.storyapp.databinding.ActivityMainBinding
import com.ari.submission.storyapp.preferences.SharedPreferences
import com.ari.submission.storyapp.ui.addstory.AddStoryActivity
import com.ari.submission.storyapp.ui.detailstories.DetailStoriesActivity
import com.ari.submission.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sph: SharedPreferences
    companion object {
        const val SUCCESS_UPLOAD_STORY = "success upload story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharedPreferences(this)
        sph.setStatusLogin(true)

        val rvstories : RecyclerView = binding.rvStories

        showStories()
        getNewStories()

    }

    private fun showStories(){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${sph.getUserToken()}"
            withContext(Dispatchers.Main) {
                val client = ApiConfig().getApiService().getAllStories(token)
                client.enqueue(object : Callback<GetAllStoryResponse> {
                    override fun onResponse(
                        call: Call<GetAllStoryResponse>,
                        response: Response<GetAllStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                val dataStories = responseBody.listStory
                                val storiesAdapter = StoriesAdapter(dataStories, object: StoriesAdapter.OnAdapterListener{
                                    override fun onClick(list: Stories) {
                                        val intent = Intent(applicationContext, DetailStoriesActivity::class.java).apply {
                                                putExtra("list_name",list.name)
                                                putExtra("list_image", list.photoUrl)
                                                putExtra("list_description", list.description)
                                                }
                                        val bundle = Bundle(ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.rvStories,"photo").toBundle())
                                        startActivity(intent,bundle)


                                    }

                                })

                                binding.rvStories.apply {
                                    layoutManager = LinearLayoutManager(this@MainActivity)
                                    setHasFixedSize(true)
                                    storiesAdapter.notifyDataSetChanged()
                                    adapter = storiesAdapter
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun getNewStories() {
        binding.apply {
            if (intent != null) {
                val isNewStory = intent.extras?.getBoolean(SUCCESS_UPLOAD_STORY)
                if (isNewStory != null && isNewStory) {
                    showStories()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_logout->{
                val builder = AlertDialog.Builder(this)
                with(builder)
                {
                    setTitle("Log Out")
                    setMessage("Are you sure to log out?")
                    setPositiveButton("Yes") { dialogInterface, which ->
                        sph.clearUserLogin()
                        sph.clearUserToken()
                        sph.setStatusLogin(false)
                        Intent(this@MainActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }

                    }
                    setNegativeButton("Cancel"){ dialogInterface, which ->
                        Toast.makeText(this@MainActivity, "Okay! Have a nice use StoryApp", Toast.LENGTH_SHORT).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }
            R.id.btn_add_photo->{
                Intent(this, AddStoryActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}