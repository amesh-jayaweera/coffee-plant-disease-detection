package com.example.coffeeplantdiseasedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<ImageView>(R.id.img_scan)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_diseaseDiagnosisFragment)
        }

        val btnMap = view.findViewById<ImageView>(R.id.img_map)
        btnMap.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_mapsFragment)
        }

        val btnBlog = view.findViewById<ImageView>(R.id.img_blog)
        btnBlog.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_blogFragment)
        }
        return view
    }
}