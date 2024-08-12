package com.project.laybare.fragment.textResult

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding
import com.project.laybare.databinding.FragmentTextResultBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class TextResult : Fragment() {
    private var _binding : FragmentTextResultBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : TextResultViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = arguments?.getString("ExtractedText", "")?:""
        mViewModel.setTextResult(text)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTextResultBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        mBinding.TextResultText.setText(mViewModel.getEditedText())
        initListener()
    }

    private fun initListener() {
        mBinding.TextResultText.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mViewModel.setEditedText(text.toString())
            }
        })

        mBinding.TextResultReset.setOnClickListener {
            mViewModel.resetText()
            mBinding.TextResultText.setText(mViewModel.getEditedText())
        }




    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}