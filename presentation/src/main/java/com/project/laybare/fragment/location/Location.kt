package com.project.laybare.fragment.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.project.domain.entity.SearchLandmarkEntity
import com.project.laybare.R
import com.project.laybare.databinding.FragmentLocationBinding
import com.project.laybare.dialog.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Location : Fragment(), OnMapReadyCallback{
    private var _binding : FragmentLocationBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mNavController: NavController
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

        mNavController = findNavController()

        if(mViewModel.isLocationDataValid()){
            initObserver()
            initListener()
            initUI()
        }else{
            createDialog("위치 데이터를 가져오지 못했어요...", true)
        }


    }


    override fun onMapReady(map: GoogleMap) {
        val data = mViewModel.getLocationData()

        val latlng = LatLng(data?.latitude?.toDouble()?:0.0, data?.longitude?.toDouble()?:0.0)
        map.apply {
            addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(data?.description?:"")
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f))
        }
    }

    private fun initObserver(){

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mApiError.collectLatest {
                createDialog(it, false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.mSetAddressText.collectLatest {
                mBinding.LocationAddress.text = it
            }
        }
    }

    private fun initListener() {
        val bottomSheetBehavior = BottomSheetBehavior.from(mBinding.LocationBottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        mBinding.LocationBottomSheet.setBackgroundResource(R.color.white)
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        mBinding.LocationBottomSheet.setBackgroundResource(R.drawable.bottom_dialog_bg)
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mBinding.LocationBottomSheet.setBackgroundResource(R.drawable.bottom_dialog_bg)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0.3f && slideOffset < 0.6f) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                }
            }
        })
    }


    private fun initUI(){
        mBinding.LocationName.text = mViewModel.getLocationName()

        val imageAdapter = mViewModel.getImageListAdapter()
        mBinding.LocationPictureList.apply {
            setHasFixedSize(true)
            if(itemDecorationCount == 0){
                addItemDecoration(LocationImageDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
            adapter = imageAdapter
        }




    }


    private fun createDialog(msg : String, isPop : Boolean) {
        val constructor = AlertDialog(requireContext(), resources.displayMetrics.widthPixels)
        val dialog = constructor.createDialog(1, msg, "확인")
        dialog.setCancelable(!isPop)
        constructor.setItemClickListener(object  : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                dialog.dismiss()
                if(isPop){
                    mNavController.popBackStack()
                }
            }
            override fun onClickCancel() {}
        })

        dialog.show()
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