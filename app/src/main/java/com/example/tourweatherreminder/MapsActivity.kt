package com.example.tourweatherreminder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var latitude: Double = 37.506361
    var longitude: Double = 127.026395
    var placeName: String = "선택한 장소"

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var loc = LatLng(latitude, longitude)
    val arrLoc = ArrayList<LatLng>()

    private lateinit var mMap: GoogleMap // onMapReady에서 초기화 됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // onMapReadyCallback 인터페이스 구현
        init()
    }


    fun init() {
        searchBtn.setOnClickListener {
            var intent = VanillaPlacePicker.Builder(this)
                .withLocation(latitude, longitude) // 이전 위치에서 시작하게
                .with(PickerType.AUTO_COMPLETE)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .setCountry("KR")
                .build()

            startActivityForResult(intent, KeyUtils.REQUEST_PLACE_PICKER)
        }
        okBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("placeName", placeName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                KeyUtils.REQUEST_PLACE_PICKER -> {
                    val vanillaAddress = VanillaPlacePicker.onActivityResult(data)
                    vanillaAddress?.let {
                        latitude = it.latitude!!
                        longitude = it.longitude!!
                        placeName = it.name!!
//                        Toast.makeText(this, it.latitude.toString() + " " + it.longitude.toString(), Toast.LENGTH_SHORT).show()
                        var selectedLocation = LatLng(latitude, longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15F))
                        mMap.addMarker(MarkerOptions().position(selectedLocation).title(placeName))

                    }

                }
            }
        }
    }


    // OnMapReadyCallback - 맵이 사용할 준비가 다 됐어~
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val myLocation = LatLng(latitude, longitude) // 현재 위치
        mMap.addMarker(
            MarkerOptions().position(myLocation).title("선택한 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .snippet("이 위치로 선택하시겠습니까?")
        ).showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.0f))
        initMapListener()
    }

    fun initMapListener() {
        mMap.setOnMapClickListener {
            mMap.clear()
            latitude = it.latitude
            longitude = it.longitude
            mMap.addMarker(
                MarkerOptions().position(it).title("선택한 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .snippet("이 위치로 선택하시겠습니까?")
            ).showInfoWindow()
        }
    }


}
