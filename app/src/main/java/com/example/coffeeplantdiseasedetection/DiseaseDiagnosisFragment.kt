package com.example.coffeeplantdiseasedetection

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [DiseaseDiagnosisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiseaseDiagnosisFragment : Fragment() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var uploadedImageView: ImageView
    private var contentUri: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_disease_diagnosis, container, false)
        val button = view.findViewById<ImageView>(R.id.btn_back)
        uploadedImageView = view.findViewById(R.id.img_upload) as ImageView
        button.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val btnPredict = view.findViewById<Button>(R.id.btn_predict)
        btnPredict.setOnClickListener {
            if (contentUri == Uri.EMPTY) {
                Toast.makeText(context, "Please upload coffee plant image", Toast.LENGTH_SHORT).show()
            } else {
                contentUri = Uri.EMPTY
                Navigation.findNavController(view).navigate(R.id.action_diseaseDiagnosisFragment_to_diseaseDiagnosisResultFragment)
            }
        }

        uploadedImageView.setOnClickListener {
            showPictureDialog()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permission = android.Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
            } else {
                // Permission has been denied
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private val galleryActivityResult = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                contentUri = data.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, contentUri)
                    // saveImage(bitmap)
                    uploadedImageView.background = null
                    uploadedImageView.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed open your gallery", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.extras != null) {
                contentUri = data.data!!
                val thumbnail = data.extras!!.get("data") as Bitmap
                uploadedImageView.background = null
                uploadedImageView.setImageBitmap(thumbnail)
                // saveImage(thumbnail)
                Toast.makeText(context, "Photo Show!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chooseImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResult.launch(galleryIntent)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraActivityResult.launch(cameraIntent)
    }
}