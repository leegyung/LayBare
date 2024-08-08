package com.project.laybare.Location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.project.domain.entity.SearchLandmarkEntity
import com.project.laybare.databinding.FragmentLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Location : Fragment(), OnMapReadyCallback{
    private var _binding : FragmentLocationBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : LocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        mBinding.LocationMap.onCreate(savedInstanceState)
        mBinding.LocationMap.getMapAsync(this)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        if(mViewModel.dataInitializeRequire()){
            val dataJson = arguments?.getString("location", "")
            val data = Gson().fromJson(dataJson, SearchLandmarkEntity::class.java)

            mViewModel.setLocationData(data)
            mViewModel.getAddress()
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        val data = mViewModel.getLocationData()

        val latlng = LatLng(data?.latitude?.toDouble()?:0.0, data?.longitude?.toDouble()?:0.0)
        p0.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(data?.description?:"")
        )
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f))
    }

    private fun initObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mSetAddressText.collectLatest {
                mBinding.LocationAddress.text = it
            }
        }
    }

    private fun initUI(){
        initObserver()
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