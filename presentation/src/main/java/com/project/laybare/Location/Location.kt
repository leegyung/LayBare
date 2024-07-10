package com.project.laybare.Location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.laybare.R
import com.project.laybare.databinding.FragmentLocationBinding


class Location : Fragment(), OnMapReadyCallback{
    private var _binding : FragmentLocationBinding? = null
    private val mBinding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        mBinding.LocationMap.onCreate(savedInstanceState)
        mBinding.LocationMap.getMapAsync(this)

        return _binding?.root
    }

    override fun onMapReady(p0: GoogleMap) {
        val sydney = LatLng(48.85837, 2.2944813)
        p0.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
    }

    override fun onPause() {
        super.onPause()
        mBinding.LocationMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.LocationMap.onResume()
    }

    override fun onStart() {
        super.onStart()
        mBinding.LocationMap.onStart()
    }

    override fun onStop() {
        super.onStop()
        mBinding.LocationMap.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}