package com.example.coffeeplantdiseasedetection

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
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
    private lateinit var spinner: Spinner
    private var image: Bitmap? = null
    private var selectedDistrictIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_disease_diagnosis, container, false)
        val button = view.findViewById<ImageView>(R.id.btn_back)
        spinner = view.findViewById<Spinner>(R.id.spinner)
        uploadedImageView = view.findViewById(R.id.img_upload) as ImageView
        button.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val btnPredict = view.findViewById<Button>(R.id.btn_predict)
        btnPredict.setOnClickListener {
            if (image == null) {
                Toast.makeText(context, "Please upload coffee plant image", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle()
                bundle.putParcelable("image", image)
                bundle.putString("district", if (selectedDistrictIndex > 0) DISTRICTS[selectedDistrictIndex] else null)
                image = null
                selectedDistrictIndex = 0
                Navigation.findNavController(view).navigate(R.id.action_diseaseDiagnosisFragment_to_diseaseDiagnosisResultFragment, bundle)
            }
        }

        val adapter = object : ArrayAdapter<String>(requireContext(), R.layout.item_district, R.id.selectedDistrict, DISTRICTS) {
            // expanded state
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)

                // Set arrow icon based on the position
                val arrowIcon = view.findViewById<ImageView>(R.id.arrowIcon)
                if (position == 0) {
                    // Show up arrow for first option in dropdown
                    arrowIcon.setImageResource(R.drawable.arrow_up)
                } else {
                    // Do not show for other options
                    arrowIcon.setImageDrawable(null)
                }

                val selectedItem = view.findViewById<TextView>(R.id.selectedDistrict)
                if (position != 0 && position == selectedDistrictIndex) {
                    selectedItem?.setTextColor(resources.getColor(R.color.black))
                } else {
                    selectedItem?.setTextColor(resources.getColor(R.color.ash_gray))
                }

                return view
            }

            // closed state
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                // Set down arrow icon for the first element
                val arrowIcon = view.findViewById<ImageView>(R.id.arrowIcon)
                if (position == 0) {
                    arrowIcon.setImageResource(R.drawable.arrow_down)
                } else {
                    // Hide arrow for other items in closed spinner
                    arrowIcon.setImageDrawable(null)
                }

                return view
            }
        }

        spinner.adapter = adapter
        selectedDistrictIndex = 0
        spinner.setSelection(0)

        // Set a listener to handle arrow icon changes when an item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Change the arrow icon when an item is selected
                selectedDistrictIndex = position
                val selectedItem = selectedItemView?.findViewById<TextView>(R.id.selectedDistrict)
                if (position != 0) {
                    selectedItem?.setTextColor(resources.getColor(R.color.black))
                } else {
                    selectedItem?.setTextColor(resources.getColor(R.color.ash_gray))
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle when nothing is selected if needed
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
                val contentUri = data.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, contentUri)
                    image = bitmap
                    uploadedImageView.setBackgroundResource(R.drawable.image_preview_backhground)
                    uploadedImageView.scaleType = ImageView.ScaleType.FIT_CENTER
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
                val thumbnail = data.extras!!.get("data") as Bitmap
                image = thumbnail
                uploadedImageView.setBackgroundResource(R.drawable.image_preview_backhground)
                uploadedImageView.scaleType = ImageView.ScaleType.FIT_CENTER
                uploadedImageView.setImageBitmap(thumbnail)
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

    companion object {
         val DISTRICTS = arrayOf(
             "Select district",
            "Ampara",
            "Anuradhapura",
            "Badulla",
            "Batticaloa",
            "Colombo",
            "Galle",
            "Hambantota",
            "Jaffna",
            "Kalutara",
            "Kandy",
            "Kegalle",
            "Kilinochchi",
            "Kurunegala",
            "Mannar",
            "Matale",
            "Matara",
            "Monaragala",
            "Mullaitivu",
            "Nuwara Eliya",
            "Polonnaruwa",
            "Puttalam",
            "Ratnapura",
            "Trincomalee",
            "Vavuniya"
        )
    }
}