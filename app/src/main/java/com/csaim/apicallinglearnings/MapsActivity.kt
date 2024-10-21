package com.csaim.apicallinglearnings

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csaim.apicallinglearnings.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var currentAddress: Address
    private lateinit var mapsManager:MapsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val GWU = LatLng(38.90226258290317, -77.05009169876575)
//        mMap.addMarker(MarkerOptions().position(GWU).title("GWU"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(GWU))


        mMap.setOnMapLongClickListener { latLng ->
            Log.d("mmmmmmmmmmap", "long clicked at ${latLng.latitude}, ${latLng.longitude}")

            lifecycleScope.launch {
                val addresses=getAddressesFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    this@MapsActivity

                )
                if(addresses.isNullOrEmpty()){
                    Toast.makeText(this@MapsActivity, "no results", Toast.LENGTH_SHORT).show()
                }else{
                    currentAddress=addresses[0]
                    val state = currentAddress.adminArea // Fetching the state
                    Log.d("Map", "State is $state")
                    mMap.addMarker(MarkerOptions().position(latLng).title(state))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                    val apikey=getString(R.string.apiKey)

                    val newsManager=MapsManager()
                    var news= listOf<MapsData>()
                    lifecycleScope.launch {
                        withContext(IO) {
                            news = newsManager.retrieveNEWS(apikey, state)
                        }
                        Log.d("NewsAPI", "Fetched news: ${news.size} items") // Log the news size

                        // Ensure news is populated before setting the adapter
                        if (news.isNotEmpty()) {
                            val adapter = MapsAdapter(news)
                            binding.newsRecyclerView.layoutManager = LinearLayoutManager(this@MapsActivity,LinearLayoutManager.HORIZONTAL, false)
                            binding.newsRecyclerView.adapter = adapter
                            binding.newsRecyclerView.visibility = RecyclerView.VISIBLE // Make sure it's visible
                            binding.newsTitleTextView.visibility = View.VISIBLE
                            binding.newsTitleTextView.text="Results for $state"
                        } else {
                            Log.d("NewsAPI", "No news data returned")
                            Toast.makeText(this@MapsActivity, "No news available", Toast.LENGTH_SHORT).show()
                        }
                    }


                }

            }
        }
    }

    private suspend fun getAddressesFromLocation(latitude:Double, longitude:Double, context:Context) : List<Address>?{

        return withContext(Dispatchers.IO){
            try {
                val geocoder=Geocoder(context)
                val addresses=geocoder.getFromLocation(latitude,longitude,5)
                addresses?.ifEmpty{
                    null
                }
            }catch (e:IOException){
                e.printStackTrace()
                listOf()
            }
        }
    }
}