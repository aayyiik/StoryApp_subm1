package com.ari.submission.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ari.submission.storyapp.R
import com.ari.submission.storyapp.background.AddNewStoryResponse
import com.ari.submission.storyapp.background.ApiConfig
import com.ari.submission.storyapp.databinding.ActivityAddStoryBinding
import com.ari.submission.storyapp.preferences.SharedPreferences
import com.ari.submission.storyapp.ui.home.MainActivity
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.concurrent.Executors

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null
    private lateinit var sph: SharedPreferences

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sph = SharedPreferences(this)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnOpenCamera.setOnClickListener {
            startCamera()
        }

        binding.btnOpenGalery.setOnClickListener {
            startGallery()
        }


        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            val result = BitmapFactory.decodeFile( myFile?.path)

            binding.imgView.setImageBitmap(result)
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.ari.submission.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile

            binding.imgView.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            uploadImageToServer(imageMultipart)

        } else {
            Toast.makeText(this@AddStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToServer(img: MultipartBody.Part){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val description = binding.edAddDescription.text.trim().toString().toRequestBody("text/plain".toMediaType())
        scope.launch {
            val token = "Bearer ${sph.getUserToken()}"
            withContext(Dispatchers.Main) {
                val client = ApiConfig().getApiService().uploadStories(token,img,description)
                client.enqueue(object: Callback<AddNewStoryResponse>{
                    override fun onResponse(
                        call: Call<AddNewStoryResponse>,
                        response: Response<AddNewStoryResponse>
                    ) {
                        if(response.isSuccessful){
                            val responseBody = response.body()
                            if (responseBody!=null && !responseBody.error){
                                Intent(
                                    this@AddStoryActivity,
                                    MainActivity::class.java
                                ).also { intent ->
                                    intent.putExtra(MainActivity.SUCCESS_UPLOAD_STORY, true)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                }
                            }
                        }else{
                            Toast.makeText(this@AddStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                        Toast.makeText(
                            this@AddStoryActivity,
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
        }
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