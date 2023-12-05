package com.example.coffeeplantdiseasedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.coffeeplantdiseasedetection.repository.ApiRepository
import com.example.coffeeplantdiseasedetection.repository.Repository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment() {

    private lateinit var repository: Repository
    private var layoutInflater: LayoutInflater? = null

    private val callback = OnMapReadyCallback { googleMap ->

        val sriLankaBounds = LatLngBounds(
            LatLng(5.9515, 79.743), // Southwest corner of Sri Lanka
            LatLng(9.9245, 81.723) // Northeast corner of Sri Lanka
        )

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(sriLankaBounds, 50))
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(7.8731, 80.7714)))

        CoroutineScope(Dispatchers.IO).launch {
            val stats = repository.statistics()

            withContext(Dispatchers.Main) {
                for (stat in stats) {
                    val district = stat.district

                    // Get district coordinates
                    val districtCoordinates = getDistrictCoordinates(district)

                    googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoContents(p0: Marker): View? {
                            return null
                        }

                        override fun getInfoWindow(marker: Marker): View? {
                            val titleSplit = marker.title?.split("|")
                            val districtName = titleSplit?.get(0)
                            val diseaseInfo = if (titleSplit?.size!! > 1) titleSplit?.get(1) else ""

                            val infowindowView = layoutInflater?.inflate(R.layout.infowindow_content, null)
                            infowindowView?.findViewById<TextView>(R.id.tooltip_district_name)?.text = districtName ?: "Unknown"
                            infowindowView?.findViewById<TextView>(R.id.tooltip_disease_info)?.text = diseaseInfo
                            return infowindowView
                        }
                    })

                    // Create marker with tooltip
                    val marker = MarkerOptions()
                        .position(LatLng(districtCoordinates.latitude, districtCoordinates.longitude))
                        .title("${district} | ${"Cerscospora: ${stat.cerscospora}\n" +
                                " Leaf rust: ${stat.leafRust}\n" +
                                " Miner: ${stat.miner}\n" +
                                " Phoma: ${stat.phoma}"}")


                    // Add marker to map
                    googleMap.addMarker(marker)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        repository = ApiRepository()
        layoutInflater = inflater

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        layoutInflater = null
    }

    private fun getDistrictCoordinates(district: String): LatLng {
        when (district) {
            "Ampara" -> return LatLng(6.9915, 80.635)
            "Anuradhapura" -> return LatLng(8.4467, 80.436)
            "Badulla" -> return LatLng(6.8125, 81.045)
            "Batticaloa" -> return LatLng(7.2570, 81.723)
            "Colombo" -> return LatLng(6.9135, 79.853)
            "Galle" -> return LatLng(6.1715, 80.213)
            "Gampaha" -> return LatLng(7.1853, 79.910)
            "Hambantota" -> return LatLng(6.0985, 80.767)
            "Jaffna" -> return LatLng(9.6635, 80.004)
            "Kalutara" -> return LatLng(6.07, 79.983)
            "Kandy" -> return LatLng(7.2905, 80.608)
            "Kegalle" -> return LatLng(7.2890, 80.304)
            "Kilinochchi" -> return LatLng(9.8265, 80.042)
            "Kurunegala" -> return LatLng(7.4945, 80.332)
            "Mannar" -> return LatLng(9.9245, 79.743)
            "Matale" -> return LatLng(7.4365, 80.548)
            "Matara" -> return LatLng(5.9515, 80.564)
            "Monaragala" -> return LatLng(6.0565, 81.143)
            "Mullaitivu" -> return LatLng(9.9135, 80.062)
            "Nuwara Eliya" -> return LatLng(6.9845, 80.759)
            "Polonnaruwa" -> return LatLng(7.1475, 80.984)
            "Puttalam" -> return LatLng(9.5205, 79.764)
            "Ratnapura" -> return LatLng(6.6940, 80.398)
            "Trincomalee" -> return LatLng(8.6225, 81.013)
            "Vavuniya" -> return LatLng(9.7375, 80.114)
            else -> return LatLng(0.0, 0.0) // Default coordinates for invalid district
        }
    }
}