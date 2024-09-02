package com.project.laybare.fragment.similarImage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
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
                arrayListOf("good, good, good, good"),
                onBackClicked = {

                },
                onImageClicked = {

                },
                mViewModel
            )
        }

    }
}