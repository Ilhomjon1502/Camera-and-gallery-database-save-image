package com.ilhomjon.cameraandgallery

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.ilhomjon.cameraandgallery.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    lateinit var binding: ActivityCameraBinding
    lateinit var currentImagePath: String
    val REQUEST_CODE = 1
    lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.oldMethodCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            val photoFile = createImageFile()
            photoFile.also {
                val photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
        binding.newMethodCamera.setOnClickListener {
            val imageFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID,
                imageFile
            )
            println("mageFile: $imageFile, photoUri = $photoUri")
            getTakeImageContent.launch(photoUri)
        }
        binding.clearImageC.setOnClickListener {

        }
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            println("getTakeImageContent ishlayapti")
            if (it) {
                println(photoUri)
                binding.imageCamera.setImageURI(photoUri)
                val inputStream = contentResolver?.openInputStream(photoUri)
                val file = File(filesDir, "image.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                val absolutePath = file.absolutePath
                Toast.makeText(this, "$absolutePath", Toast.LENGTH_SHORT).show()
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        println("createImageFile ishlayapti")
        return File.createTempFile("JPEG_$format", ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    //eski versiya
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("onActivityResult ishlayapti if ichida emas lekin")
        if (::currentImagePath.isInitialized) {
            println("CurrentImagePath = $currentImagePath \n onActivityResult ishlayapti")
            binding.imageCamera.setImageURI(Uri.fromFile(File(currentImagePath)))
            //imageView ga rasm qo'ymayapti
        }
    }
}