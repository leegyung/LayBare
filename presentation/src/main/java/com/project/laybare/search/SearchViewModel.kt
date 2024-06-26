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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {
    private var mNetworkingJob : Job? = null

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

    fun getAdapter(listener : SearchAdapterInterface?) : SearchAdapter {
        mAdapter.setListener(listener)
        return mAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getNewKeyword() {
        mNetworkingJob?.cancel()
        mNetworkingJob = viewModelScope.launch {
            mSearchResult.clear()
            mTotalCount = 0
            mCurrentPage = 0

            mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, 1, 10).collectLatest { result ->
                val data = result.data
                if(result is ApiResult.ResponseSuccess && data != null){
                    setNewImagePage(data)
                    mAdapter.notifyDataSetChanged()
                }else{

                }
            }

        }
    }

    fun getNextPage() {
        if(mTotalCount <= mSearchResult.size) {
            return
        }
        mNetworkingJob?.cancel()
        mNetworkingJob = viewModelScope.launch {
            mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, mCurrentPage + 1, 10).collectLatest { result ->
                val data = result.data
                if(result is ApiResult.ResponseSuccess && data != null){
                    val oldSize = mSearchResult.size
                    setNewImagePage(data)
                    mAdapter.notifyItemRangeInserted(oldSize, mSearchResult.size - 1)
                }else{

                }
            }
        }
    }


    private fun setNewImagePage(data : SearchImageResultEntity) {
        mCurrentPage ++
        mTotalCount = data.totalResults
        mSearchResult.addAll(data.imageList)
    }






}