package com.project.laybare.fragment.similarImage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.project.laybare.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarImage : Fragment() {

    private lateinit var mComposeView : ComposeView
    private val mViewModel: SimilarImageViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            mComposeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mComposeView.setContent {
            SimilarImageCompose(
                mViewModel,
                onBackClicked = {
                    findNavController().popBackStack()
                },
                onKeywordClicked = { index ->
                    mViewModel.onKeywordClicked(index)
                },
                onImageClicked = {
                    moveToImageDetail(it)
                }
            )
        }

    }


    private fun moveToImageDetail(index : Int) {
        val setDataResult = mViewModel.onImageClicked(index)
        if(setDataResult){
            val navOption = NavOptions.Builder()
                .setEnterAnim(R.anim.next_page_in_anim)
                .setExitAnim(R.anim.previous_page_out_anim)
                .setPopEnterAnim(R.anim.previous_page_in_anim)
                .setPopExitAnim(R.anim.next_page_out_anim)
                .build()
            findNavController().navigate(R.id.imageDetail, null, navOption)
        }
    }







}

