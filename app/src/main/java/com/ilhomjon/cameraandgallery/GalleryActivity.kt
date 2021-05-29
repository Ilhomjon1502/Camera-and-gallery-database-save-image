package com.ilhomjon.cameraandgallery

import DB.MyDbHelper
import Models.ImageModels
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ilhomjon.cameraandgallery.databinding.ActivityGalleryBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class GalleryActivity : AppCompatActivity() {

    val CODE_REQUEST = 1
    lateinit var binding: ActivityGalleryBinding
    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)

        binding.oldMethod.setOnClickListener {
            pickImageFromGalleryOld()
        }
        binding.newMethod.setOnClickListener {
            pickImageFromGalleryNew()
        }
        binding.clearImageG.setOnClickListener {
            clearImages()
        }
    }

    private fun pickImageFromGalleryOld() {
        startActivityForResult(
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            },
            CODE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_REQUEST && resultCode == Activity.RESULT_OK){
            val uri = data?.data ?: return
            binding.imageGallery.setImageURI(uri)
            val inputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            Toast.makeText(this, "$absolutePath", Toast.LENGTH_SHORT).show()
        }
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()){ uri:Uri->
            uri ?: return@registerForActivityResult
            binding.imageGallery.setImageURI(uri)
            val inputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath

            val fileInputStream = FileInputStream(file)
            val readBytes = fileInputStream.readBytes()

            val imageModels = ImageModels(absolutePath, readBytes)
            myDbHelper.insertImage(imageModels)

            myDbHelper.getAllImages()
            Toast.makeText(this, "$absolutePath", Toast.LENGTH_SHORT).show()
        }

    private fun pickImageFromGalleryNew() {
        getImageContent.launch("image/*")
    }

    private fun clearImages() {
        val externalFilesDir = filesDir
        if (externalFilesDir?.isDirectory == true){
            val listFiles = externalFilesDir.listFiles()
            if (listFiles?.isEmpty() == true){
                Toast.makeText(this, "No image", Toast.LENGTH_SHORT).show()
                return
            }
            listFiles?.forEach {
                println(it)
                it.delete()
            }
        }
    }
}