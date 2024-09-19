package com.project.laybare.fragment.search

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.usecase.SearchImagePagingUseCase
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mSearchPictureUseCase: SearchImageUseCase,
    private val mSearchImagePagingUseCase : SearchImagePagingUseCase
) : ViewModel() {
    private var mNetworkingJob : Job? = null
    private val _createAlert = MutableSharedFlow<String>()

    val mCreateAlert = _createAlert.asSharedFlow()

    var mKeywordState = mutableStateOf("")
        private set

    var mImageListState: MutableStateFlow<PagingData<ImageEntity>> = MutableStateFlow(value = PagingData.empty())
        private set



    private var mKeyword = ""
    private val mSearchResult = arrayListOf<ImageEntity>()
    private val mAdapter = SearchAdapter(mSearchResult)

    private var mTotalCount : Long = 0
    private var mCurrentPage = 1



    fun searchImage() {
        val keyword = mKeywordState.value

        if(keyword.isEmpty()){
            return
        }

        mNetworkingJob?.cancel()
        mNetworkingJob = viewModelScope.launch {
            mImageListState.value = PagingData.empty()

            mSearchImagePagingUseCase(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, keyword, 10)
                .cachedIn(viewModelScope)
                .distinctUntilChanged()
                .collect{ result ->
                    mImageListState.value = result
                }
        }


    }




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