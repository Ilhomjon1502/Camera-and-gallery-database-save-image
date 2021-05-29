package com.ilhomjon.cameraandgallery

import DB.MyDbHelper
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilhomjon.cameraandgallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDbHelper: MyDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)

        binding.btnGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
        binding.btnCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val list = myDbHelper.getAllImages()
        if (list.isNotEmpty()) {
            binding.imagePath.setImageURI(Uri.parse(list[0].imagePath))

            val bitmap = BitmapFactory.decodeByteArray(list[0].image, 0, list[0].image?.size!!)
            binding.imageByte.setImageBitmap(bitmap)
        }
    }
}