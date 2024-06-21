package com.project.laybare.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.project.laybare.databinding.FragmentHomeBinding
import com.project.laybare.home.viewmodel.HomeViewModel
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
        val layoutManager = GridLayoutManager(this.context, 2)
        val adapter = mViewModel.getHomeAdapter()

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    3 -> 1
                    else -> 2
                }
            }
        }


        mBinding.HomeRecyclerView.apply {
            setHasFixedSize(true)
            this.layoutManager =layoutManager
            this.adapter = adapter
        }
    }








    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}