package com.project.laybare.search

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {
    private var mNetworkingJob : Job? = null
    private val _createAlert = MutableSharedFlow<String>()

    val mCreateAlert = _createAlert.asSharedFlow()


    private var mKeyword = ""
    private val mSearchResult = arrayListOf<ImageEntity>()
    private val mAdapter = SearchAdapter(mSearchResult)

    private var mTotalCount : Long = 0
    private var mCurrentPage = 0


    fun getKeyword() : String {
        return mKeyword
    }

    fun setKeyword(str : String){
        mKeyword = str
    }

    fun getAdapter() : SearchAdapter {
        return mAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getNewKeyword() {
        mNetworkingJob?.cancel()
        mNetworkingJob = viewModelScope.launch {
            mSearchResult.clear()
            mTotalCount = 0
            mCurrentPage = 0

            val result = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, 1, 10)
            if(result is ApiResult.ResponseSuccess && result.data != null){
                setNewImagePage(result.data!!)
                mAdapter.notifyDataSetChanged()
            }else{
                _createAlert.emit(result.errorMessage?:"데이터 로딩 실패")
            }
        }
    }

    fun getNextPage() {
        if(mTotalCount <= mSearchResult.size) {
            return
        }
        mNetworkingJob?.cancel()
        mNetworkingJob = viewModelScope.launch {
            val result = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, mCurrentPage + 1, 10)

            if (result is ApiResult.ResponseSuccess && result.data != null) {
                val oldSize = mSearchResult.size
                setNewImagePage(result.data!!)
                mAdapter.notifyItemRangeInserted(oldSize, mSearchResult.size - 1)
            } else {
                _createAlert.emit(result.errorMessage ?: "데이터 로딩 실패")
            }
        }
    }


    private fun setNewImagePage(data : SearchImageResultEntity) {
        mCurrentPage ++
        mTotalCount = data.totalResults
        mSearchResult.addAll(data.imageList)
    }






}