package com.project.laybare.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {



    fun test() {
        viewModelScope.launch {
            mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, "키크론 q1", 1, 10).collectLatest { result ->
                if(result is ApiResult.ResponseSuccess){
                    val data = result.data
                    if(data == null) {
                        val test = result.data
                        val test2 = test?.imageList
                    }else{
                        val test = result.data
                        val test2 = test?.imageList
                    }
                }else{
                    val i = 0
                }
            }
        }
    }


}