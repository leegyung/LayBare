package com.project.laybare.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.usecase.GetHomeImagesUseCase
import com.project.domain.util.ApiResult
import com.project.laybare.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mGetHomeImageUseCase: GetHomeImagesUseCase
) : ContainerHost<HomeUiState, HomeSideEffect>, ViewModel() {


    override val container: Container<HomeUiState, HomeSideEffect> = container(HomeUiState())


    init {
        getInitialData()
    }

    fun getInitialData() = intent {
        mGetHomeImageUseCase(
            apiKey = BuildConfig.API_KEY,
            searchEngine = BuildConfig.SEARCH_ENGINE,
            sectionSize = 2,
            bannerImageNum = 5,
            normalImageNum = 10
        ).collect{ result ->
            when(result){
                is ApiResult.ResponseError -> {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(HomeSideEffect.CreateDialog(result.errorMessage))
                }
                is ApiResult.ResponseLoading -> {
                    reduce { state.copy(isLoading = true) }
                }
                is ApiResult.ResponseSuccess -> {
                    reduce { state.copy(imageSections = result.data, isLoading = false) }
                }
            }
        }

    }

    fun onHandleEvent(event : HomeEvent) {

    }





}