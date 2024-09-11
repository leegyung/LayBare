package com.project.laybare.fragment.search

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mSearchPictureUseCase: SearchImageUseCase) : ViewModel() {
    private var mNetworkingJob : Job? = null
    private val _createAlert = MutableSharedFlow<String>()

    val mCreateAlert = _createAlert.asSharedFlow()


    private var mKeyword = ""
    private val mSearchResult = arrayListOf<ImageEntity>()
    private val mAdapter = SearchAdapter(mSearchResult)

    private var mTotalCount : Long = 0
    private var mCurrentPage = 1


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

        mSearchResult.clear()
        mTotalCount = 0
        mCurrentPage = 1

        mNetworkingJob = mSearchPictureUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, mCurrentPage, 10).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    result.data.let{
                        setNewImagePage(it)
                        mAdapter.notifyDataSetChanged()
                    }
                }
                is ApiResult.ResponseError -> {
                    _createAlert.emit(result.errorMessage?:"이미지 로딩 실패")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getNextPage() {
        if(mTotalCount <= mSearchResult.size) {
            return
        }
        mNetworkingJob?.cancel()
        mNetworkingJob = mSearchPictureUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, mKeyword, mCurrentPage + 1, 10).onEach { result ->
            when(result){
                is ApiResult.ResponseLoading -> {

                }
                is ApiResult.ResponseSuccess -> {
                    result.data.let{
                        val oldSize = mSearchResult.size
                        setNewImagePage(it)
                        mAdapter.notifyItemRangeInserted(oldSize, mSearchResult.size - 1)
                    }
                }
                is ApiResult.ResponseError -> {
                    _createAlert.emit(result.errorMessage?:"다음 페이지 로딩 실패")
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun setNewImagePage(data : SearchImageResultEntity) {
        mCurrentPage += data.imageList.size
        mTotalCount = data.totalResults
        mSearchResult.addAll(data.imageList)
    }






}