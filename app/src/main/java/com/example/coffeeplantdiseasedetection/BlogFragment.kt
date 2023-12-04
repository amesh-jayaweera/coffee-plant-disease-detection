package com.example.coffeeplantdiseasedetection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeplantdiseasedetection.adapter.BlogPostAdapter
import com.example.coffeeplantdiseasedetection.model.BlogItem

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blog, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
       val button = rootView.findViewById<ImageView>(R.id.btn_back_1)

        recyclerView.adapter = BlogPostAdapter(blogList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        button.setOnClickListener {
            Navigation.findNavController(rootView).navigateUp()
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment BlogFragment.
         */
        @JvmStatic
        fun newInstance() = BlogFragment().apply {}

        val blogList = listOf(
            BlogItem(
                title = "COFFEE LEAF RUST – THE DEADLY DISEASE",
                description = "IT IS ONE OF THE DEADLIEST COFFEE DISEASES AND HAS BEEN KNOWN TO WIPE OUT COFFEE FARMS COMPLETELY.",
                url = "https://twochimpscoffee.com/blogs/coffee-leaf-rust/"
            ),
            BlogItem(
                title = "Coffea arabica, Coffea robusta",
                description = "Coffee trees are pruned short to conserve their energy and aid in harvesting, but can grow to more than 30 feet (9 meters) high. Each tree is covered with green, waxy leaves growing opposite each other in pairs.",
                url = "https://dea.gov.lk/coffee/"
            ),
            BlogItem(
                title = "CEYLON COFFEE FROM SRI LANKA",
                description = "The history of Ceylon Coffee is even longer than that of Ceylon Tea, a global household name today. The dawn of the Ceylon Coffee era befell circa 1780. ",
                url = "https://www.srilankabusiness.com/food-and-beverages/coffee-from-sri-lanka.html"
            ),
            BlogItem(
                title = "Coffee Leaf Miner",
                description = "Leucoptera coffeella or Coffee Leaf Miner, is an insect pest not yet found on the Hawaiian islands.",
                url = "https://www.hawaiicoffeeed.com/coffee-leaf-miner---nko.html"
            ),
            BlogItem(
                title = "Coffee Leaf Miner",
                description = "Leucoptera coffeella or Coffee Leaf Miner, is an insect pest not yet found on the Hawaiian islands.",
                url = "https://www.hawaiicoffeeed.com/coffee-leaf-miner---nko.html"
            ),
            BlogItem(
                title = "Coffee Leaf Miner",
                description = "Leucoptera coffeella or Coffee Leaf Miner, is an insect pest not yet found on the Hawaiian islands.",
                url = "https://www.hawaiicoffeeed.com/coffee-leaf-miner---nko.html"
            )
        )
    }
}