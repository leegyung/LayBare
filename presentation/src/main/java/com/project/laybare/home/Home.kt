package com.project.laybare.home

import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.laybare.R
import com.project.laybare.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initUI()

        if(mViewModel.requireImageData()){
            mViewModel.getInitialData()
        }

    }

    private fun initObserver() {

    }

    private fun initUI() {
        mBinding.HomeRecyclerView.apply {
            setHasFixedSize(true)
            adapter = mViewModel.getHomeAdapter()
        }
    }








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}