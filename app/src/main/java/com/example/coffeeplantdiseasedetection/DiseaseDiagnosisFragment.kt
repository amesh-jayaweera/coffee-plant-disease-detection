package com.example.coffeeplantdiseasedetection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 * Use the [DiseaseDiagnosisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiseaseDiagnosisFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_disease_diagnosis, container, false)
        val button = view.findViewById<ImageView>(R.id.btn_back)
        button.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }
        return view
    }
}