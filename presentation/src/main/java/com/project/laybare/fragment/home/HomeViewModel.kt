package com.project.laybare.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.usecase.GetHomeImagesUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mGetHomeImageUseCase: GetHomeImagesUseCase) : ViewModel() {
    private val _apiError = MutableSharedFlow<String>()
    private val _loadingState = MutableSharedFlow<Boolean>()

    val mApiError = _apiError.asSharedFlow()
    val mLoadingState = _loadingState.asSharedFlow()

    private val mSectionList = arrayListOf<HomeListSectionData>()
    private val mHomeAdapter = HomeAdapter(mSectionList)

    init {
        getInitialData()
    }

    /**
     * RandomWordGenerator 를 통해 받아온 랜덤 단어 5의 사진 리스트 요청.
     * 받아온 사진 리스트와 viewType값 을 포함한 HomeListSectionData 를 mSectionList 에 추가,
     * mHomeAdapter 에 notify
     * 하... 한번 호출로 몽땅 받아오고싶다....
     */
    fun getInitialData() {
        viewModelScope.launch {
            mGetHomeImageUseCase(
                apiKey = BuildConfig.API_KEY,
                searchEngine = BuildConfig.SEARCH_ENGINE,
                sectionSize = 2,
                bannerImageNum = 5,
                normalImageNum = 10
            ).collect{ result ->
                when(result){
                    is ApiResult.ResponseError -> {
                        println("애러 생김")
                    }
                    is ApiResult.ResponseLoading -> {

                    }
                    is ApiResult.ResponseSuccess -> {
                        println(result.data.toString())
                    }
                }
            }
        }
    }

    private fun getArraySectionData(data : SearchImageResultEntity, sectionType : String) : HomeListSectionData? {
        if(data.imageList.isEmpty()){
            return null
        }
        return HomeListSectionData(
            sectionType,
            data.keyWord,
            data.imageList
        )
    }


    fun getHomeAdapter() : HomeAdapter {
        return mHomeAdapter
    }



}