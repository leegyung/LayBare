package com.project.laybare.home.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.databinding.FragmentHomeBinding
import com.project.laybare.home.HomeListInterface
import com.project.laybare.home.adapter.HomeDecorator
import com.project.laybare.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : HomeViewModel by viewModels()
    private lateinit var mNavController: NavController
    private var mListInterface : HomeListInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()

        initObserver()
        initListener()
        initUI()

        if(mViewModel.requireImageData()){
            //mViewModel.getInitialData()
        }

    }

    private fun initObserver() {

    }

    private fun initListener(){
        mListInterface = object : HomeListInterface{
            override fun onImageClicked(image: ImageEntity) {
                Log.v("이미지", image.link)
            }

        }


        mBinding.HomeSearchBtn.setOnClickListener {
            mNavController.navigate(R.id.action_home_to_search)
        }
    }

    private fun initUI() {
        val layoutManager = GridLayoutManager(this.context, 2)
        val adapter = mViewModel.getHomeAdapter(mListInterface)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    3 -> 1
                    else -> 2
                }
            }
        }

        mBinding.HomeRecyclerView.apply {
            if(itemDecorationCount == 0){
                addItemDecoration(HomeDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }
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