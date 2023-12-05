package com.example.coffeeplantdiseasedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeplantdiseasedetection.adapter.BlogPostAdapter
import com.example.coffeeplantdiseasedetection.repository.ApiRepository
import com.example.coffeeplantdiseasedetection.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment() {

    private lateinit var repository: Repository
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blog, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
       val button = rootView.findViewById<ImageView>(R.id.btn_back_1)

        button.setOnClickListener {
            Navigation.findNavController(rootView).navigateUp()
        }

        repository = ApiRepository()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val blogs = repository.blogs()

            withContext(Dispatchers.Main) {
                recyclerView.adapter = BlogPostAdapter(blogs)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
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
    }
}