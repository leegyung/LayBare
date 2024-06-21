package com.project.laybare.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.SearchImageResultEntity
import com.project.domain.usecase.SearchImageUseCase
import com.project.domain.util.RandomWordGenerator
import com.project.laybare.BuildConfig
import com.project.laybare.home.HomeListInterface
import com.project.laybare.home.data.HomeListSectionData
import com.project.laybare.home.adapter.HomeAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {

    private val mSectionList = arrayListOf<HomeListSectionData>()
    private val mHomeAdapter = HomeAdapter(mSectionList)

    fun requireImageData() : Boolean {
        return mSectionList.isEmpty()
    }

    /**
     * RandomWordGenerator 를 통해 받아온 랜덤 단어 5의 사진 리스트 요청.
     * 받아온 사진 리스트 데이터와 리사이클러뷰에 표시할 viewType 을 포함한 HomeListSectionData 를 mSectionList 에 추가,
     * mHomeAdapter 에 notify
     * 하... 한번 호출로 몽땅 받아오고싶다....
     */
    fun getInitialData() {
        viewModelScope.launch {
            mSectionList.clear()

            val words = RandomWordGenerator.getRandomWord(5)
            /*
            val response1 = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[0], 1, 5)
            val response2 = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[1], 1, 10)
            val response3 = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[2], 1, 10)
            val response4 = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[3], 1, 10)
            val response5 = mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[4], 1, 10)


            combine(response1, response2, response3, response4, response5) { v1, v2, v3, v4, v5 ->
                val sections = arrayListOf<HomeListSectionData>()
                v1.data?.let {
                    getArraySectionData(it, "BANNER")?.let { section -> sections.add(section) }
                }
                v2.data?.let {
                    getArraySectionData(it, "HORIZONTAL")?.let { section -> sections.add(section) }
                }
                v3.data?.let {
                    getArraySectionData(it, "HORIZONTAL")?.let { section -> sections.add(section) }
                }
                v4.data?.let {
                    getArraySectionData(it, "HORIZONTAL")?.let { section -> sections.add(section) }
                }
                v5.data?.let {
                    it.imageList.forEach { image ->
                        sections.add(getSingleImageSectionData(image))
                    }
                }
                sections
            }.collectLatest { result ->
                mSectionList.addAll(result)
                mHomeAdapter.notifyItemRangeInserted (0, mSectionList.size - 1)
            }

             */


            mUseCase.getImageList(BuildConfig.API_KEY, BuildConfig.SEARCH_ENGINE, words[0], 1, 5).collectLatest {
                val sections = arrayListOf<HomeListSectionData>()
                it.data?.let { data ->
                    getArraySectionData(data, "BANNER")?.let { section -> sections.add(section) }
                    getArraySectionData(data, "HORIZONTAL")?.let { section -> sections.add(section) }
                    getArraySectionData(data, "HORIZONTAL")?.let { section -> sections.add(section) }
                    getArraySectionData(data, "HORIZONTAL")?.let { section -> sections.add(section) }
                    data.imageList.forEach { image ->
                        sections.add(getSingleImageSectionData(image))
                    }
                }
                mSectionList.addAll(sections)
                mHomeAdapter.notifyItemRangeInserted (0, mSectionList.size - 1)
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
            null,
            data.imageList
        )
    }

    private fun getSingleImageSectionData(data : ImageEntity) : HomeListSectionData {
        return HomeListSectionData(
            "IMAGE",
            "",
            data,
            null
        )
    }


    fun getHomeAdapter(listener : HomeListInterface?) : HomeAdapter {
        mHomeAdapter.setListener(listener)
        return mHomeAdapter
    }



}