package com.project.laybare.fragment.textResult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding
import com.project.laybare.databinding.FragmentTextResultBinding
import com.project.laybare.dialog.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class TextResult : Fragment() {
    private lateinit var mComposeView : ComposeView
    private lateinit var mNavController: NavController
    private val mViewModel : TextResultViewModel by viewModels()


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

        mNavController = findNavController()

        mComposeView.setContent {
            TextResultMainScreen(
                viewModel = mViewModel,
                navController = mNavController
            )
        }
    }




}