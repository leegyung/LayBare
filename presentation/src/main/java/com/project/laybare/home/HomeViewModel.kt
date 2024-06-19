package com.project.laybare.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.domain.util.RandomWordGenerator
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {

    private val mSectionList = arrayListOf<HomeListSectionData>()
    private val mHomeAdapter = HomeAdapter(mSectionList)

    fun requireImageData() : Boolean {
        return mSectionList.isEmpty()
    }

    fun getInitialData() {
        viewModelScope.launch {
            mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, RandomWordGenerator.getRandomWord(), 1, 5).collectLatest { result ->
                if(result is ApiResult.ResponseSuccess){
                    val bannerSection = HomeListSectionData(
                        "BANNER",
                        null,
                        result.data?.imageList?: arrayListOf()
                    )
                    mSectionList.add(bannerSection)
                    mHomeAdapter.notifyItemInserted(0)
                }else{
                    val i = 0
                }
            }
        }
    }

    fun getHomeAdapter() : HomeAdapter {
        return mHomeAdapter
    }



}