package com.example.coffeeplantdiseasedetection

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

private const val ARG_PARAM_IMAGE_URI = "image"

/**
 * A simple [Fragment] subclass.
 * Use the [DiseaseDiagnosisResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiseaseDiagnosisResultFragment : Fragment() {
    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getParcelable("image")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_disease_diagnosis_result, container, false)
        val button = view.findViewById<ImageView>(R.id.btn_back)
        button.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val btnDone = view.findViewById<Button>(R.id.btn_done)
        btnDone.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val imageView = view.findViewById<ImageView>(R.id.img_view)
        imageView.setImageBitmap(image)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param image byte array of the image.
         * @return A new instance of fragment DiseaseDiagnosisResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(image: Bitmap) =
            DiseaseDiagnosisResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM_IMAGE_URI, image)
                }
            }
    }
}