package com.project.laybare.search

import androidx.lifecycle.ViewModel
import com.project.domain.usecase.SearchImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mUseCase: SearchImageUseCase) : ViewModel() {
}