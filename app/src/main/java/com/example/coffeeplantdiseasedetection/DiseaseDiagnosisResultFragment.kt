package com.example.coffeeplantdiseasedetection

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.coffeeplantdiseasedetection.repository.ApiRepository
import com.example.coffeeplantdiseasedetection.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM_IMAGE_URI = "image"

/**
 * A simple [Fragment] subclass.
 * Use the [DiseaseDiagnosisResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiseaseDiagnosisResultFragment : Fragment() {
    private lateinit var repository: Repository
    private var image: Bitmap? = null
    private lateinit var btnDone: Button
    private lateinit var textDisease: TextView
    private lateinit var textSolution: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getParcelable("image")!!
        }

        repository = ApiRepository()
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

        btnDone = view.findViewById<Button>(R.id.btn_done)
        btnDone.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val imageView = view.findViewById<ImageView>(R.id.img_view)
        imageView.setImageBitmap(image)


        textDisease = view.findViewById(R.id.txt_disease)
        textSolution = view.findViewById(R.id.txt_solution)
        btnDone.visibility = View.GONE
        textDisease.text = "N/A"
        textSolution.text = "N/A"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val predictedCategory = repository.predict(image)
            withContext(Dispatchers.Main) {
                setSolution(predictedCategory)
            }
        }
    }

    private fun setSolution(category:Int?) {
        when (category) {
            null -> {
                textDisease.text = "N/A"
                Toast.makeText(context, "Failed to diagnosis!. Try again.", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                textDisease.text = "N/A"
                Toast.makeText(context, "It's a healthy leaf!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                textDisease.text = DISEASES[category]
                textSolution.text =  Html.fromHtml(SOLUTIONS[category], Html.FROM_HTML_MODE_COMPACT)
            }
        }

        btnDone.visibility = View.VISIBLE
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


        val DISEASES = mapOf(
            0 to "Cercospora Leaf Spot",
            2 to "Leaf Rust",
            3 to "Miner",
            4 to "Phoma Leaf Rust"
        )

        val SOLUTIONS = mapOf(
            0 to """
                <ul>
                    <li>Keep a balance and controlled fertilization plan, add organic matter to your soil, and balance the shadow & lighting of your plantation. </li>
                    <br/>
                    <li>Crop rotation for 2 years may help reduce inoculum in the soil. </li>
                    <br/>
                    <li> Spray program of protectant fungicides (chlorothalonil; copper-based, mancozeb, maneb) can help reduce disease incidence and severity. </li>
                </ul>
            """.trimIndent(),
            2 to """
                <ul>
                    <li>Always consider an integrated approach along with preventive measures and available biological treatments.</li>
                    <br/>
                    <li>Prophylactic spraying of Bordeaux mixture or Copper Oxychloride 50% WG can be done once before the occurrence of favorable environmental factors to the disease and again after cessation of this period.</li>
                </ul>
            """.trimIndent(),
            3 to """
                <ul>
                    <li>Tillage to bury the plant residue or dispose them.</li>
                    <br/>
                    <li>Practice crop rotation for at least three years with non-hosts.</li>
                    <br/>
                    <li>Maintain optimal plant nutrition.</li>
                    <br/>
                    <li>Management of alternative weed hosts.</li>
                    <br/>
                    <li>Roguing out diseased beets before storage. If storing roots, maintain optimal conditions and air circulation.</li>
                </ul>
            """.trimIndent(),
            4 to """
                <ul>
                    <li>Spray neem oil products (Azadirachtin) against larvae onto leaves in the early morning or late evening.</li>
                    <br/>
                    <li>Foliar applications of the entomophagous nematode, Steinernema carpocapsae, can reduce the leaf miner population.</li>
                    <br/>
                    <li>Other biological controls of leaf miners include parasitoids (e.g. Chrysonotomyia punctiventris and Ganaspidium hunteri) and nematodes (e.g. Steinernema carpocapsae).</li>
                </ul>
            """.trimIndent()
        )
    }
}