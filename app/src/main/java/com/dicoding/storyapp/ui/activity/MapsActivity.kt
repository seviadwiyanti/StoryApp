package com.dicoding.storyapp.ui.activity

import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.pref.ResultValue

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.viewmodel.MapsViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivActionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        addManyMarker()
//        setMapStyle()
    }

//    private fun setMapStyle() {
//        try {
//            val success =
//                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
//            if (!success) {
//                Log.e(TAG, "Style parsing failed,")
//            }
//        } catch (e: Resources.NotFoundException) {
//            Log.e(TAG, "Can't find style. Error: ", e)
//        }
//    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

//    private fun addManyMarker() {
//        val markerIdMap = HashMap<Marker, String>()
//
//        viewModel.getStoriesWithLocation().observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is ResultValue.Loading -> {
//                        showLoading(true)
//                    }
//
//                    is ResultValue.Success -> {
//
//                        result.data.listStory.forEach { data ->
//                            val latLng = LatLng(data.lat, data.lon)
//
//                            val marker = mMap.addMarker(
//                                MarkerOptions()
//                                    .position(latLng)
//                                    .title(data.name)
//                                    .snippet(data.description)
//                                    .icon(
//                                        vectorToBitmap(
//                                            R.drawable.ic_baseline_maps,
//                                            Color.parseColor("#FC585C")
//                                        )
//                                    )
//                            )
//
//                            marker?.let { id ->
//                                markerIdMap[id] = data.id
//                            }
//
//                            boundsBuilder.include(latLng)
//                        }
//
//                        val bounds: LatLngBounds = boundsBuilder.build()
//                        mMap.animateCamera(
//                            CameraUpdateFactory.newLatLngBounds(
//                                bounds,
//                                resources.displayMetrics.widthPixels,
//                                resources.displayMetrics.heightPixels,
//                                300
//                            )
//                        )
//
//                        mMap.setOnInfoWindowClickListener { marker ->
//                            val id = markerIdMap[marker]
//                            if (id != null) {
//                                showStoryDetailsFromTheMarker(id)
//                            }
//                        }
//
//                        showLoading(false)
//                    }
//
//                    is ResultValue.Error -> {
//                        Toast.makeText(this, getString(R.string.empty_maps_story), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

    private fun addManyMarker() {
        val markerIdMap = HashMap<Marker, String>()

        viewModel.getStoriesWithLocation().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultValue.Loading -> {
                        showLoading(true)
                    }

                    is ResultValue.Success -> {

                        result.data.listStory.forEach { data ->
                            val lat = data.lat ?: return@forEach
                            val lon = data.lon ?: return@forEach
                            val name = data.name ?: "Unknown"
                            val description = data.description ?: "No description"

                            val latLng = LatLng(lat, lon)

                            val marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(name)
                                    .snippet(description)
                                    .icon(
                                        vectorToBitmap(
                                            R.drawable.baseline_add_location_alt_24,
                                            Color.parseColor("#FC585C")
                                        )
                                    )
                            )

                            marker?.let { id ->
                                markerIdMap[id] = data.id ?: return@forEach
                            }

                            boundsBuilder.include(latLng)
                        }

                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                300
                            )
                        )

                        mMap.setOnInfoWindowClickListener { marker ->
                            val id = markerIdMap[marker]
                            if (id != null) {
                                showStoryDetailsFromTheMarker(id)
                            }
                        }

                        showLoading(false)
                    }

                    is ResultValue.Error -> {
                        Toast.makeText(this, getString(R.string.empty_maps_story), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e(TAG_BITMAP_HELPER, "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun showStoryDetailsFromTheMarker(id: String) {
        val intentToDetail = Intent(this, StoryDetailActivity::class.java)
        intentToDetail.putExtra(STORY_ID, id)
        startActivity(intentToDetail)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
        private const val TAG_BITMAP_HELPER = "BitmapHelper"
        const val STORY_ID = "STORY_ID"
    }
}