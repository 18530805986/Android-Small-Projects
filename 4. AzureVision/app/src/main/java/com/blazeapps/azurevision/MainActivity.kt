package com.blazeapps.azurevision

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.provider.MediaStore
import android.R.attr.data
import android.app.ProgressDialog
import android.os.AsyncTask
import com.google.gson.Gson
import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.StringBuilder
import android.R.attr.bitmap
import android.net.Uri
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log


class MainActivity : AppCompatActivity() {

    companion object {
        const val PICK_IMAGE_CODE = 132
        const val THUMBNAIL_SIZE = 500.0
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }


    internal var visionServiceClient = VisionServiceRestClient(Keys.API_KEY, Keys.API_LINK)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonProcess.setOnClickListener {
            checkForPermission()
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
    }

    fun getThumbnail(uri: Uri): Bitmap? {
        var input = this.contentResolver.openInputStream(uri)

        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()

        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return null
        }

        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth

        val ratio = if (originalSize > THUMBNAIL_SIZE) (originalSize / THUMBNAIL_SIZE) else 1.0

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inDither = true //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//
        input = this.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return bitmap
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0)
            1
        else
            k
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                Toast.makeText(this, "fail to get picture", Toast.LENGTH_SHORT).show()
                return
            }
            val uri = data.data
            processBitmap(getThumbnail(uri!!)!!)

        }

    }

    private fun processBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val visionTask = object : AsyncTask<InputStream, String, String>() {
            var progressDialog = ProgressDialog(this@MainActivity)

            override fun onPreExecute() {
                progressDialog.show()
                super.onPreExecute()
            }

            override fun onProgressUpdate(vararg values: String?) {
                progressDialog.setMessage(values[0])
                super.onProgressUpdate(*values)
            }

            override fun doInBackground(vararg params: InputStream?): String {
                try {
                    publishProgress("Recognizing...")
                    val features = arrayOf("Description")
                    val details = arrayOf<String>()
                    val result = visionServiceClient.analyzeImage(params[0], features, details)

                    return Gson().toJson(result)

                } catch (e: Exception) {
                    return ""
                }
            }

            override fun onPostExecute(result: String?) {
                progressDialog.dismiss()
                val res = Gson().fromJson<AnalysisResult>(result, AnalysisResult::class.java)
                val resultText = StringBuilder()
                for (caption in res.description.captions) {
                    resultText.append(caption.text)
                }
                textViewResult.text = resultText.toString()
            }

        }

        visionTask.execute(inputStream)

    }

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            pickImageFromGallery()
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
             MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Log.e(TAG, "WE GOT NO PERMISSION")
                }
            }
        }
    }

    private val TAG = MainActivity::class.java.simpleName
}
