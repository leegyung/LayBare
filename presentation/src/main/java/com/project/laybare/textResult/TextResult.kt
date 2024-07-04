package com.project.laybare.textResult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.laybare.R
import com.project.laybare.databinding.FragmentImageDetailBinding
import com.project.laybare.databinding.FragmentTextResultBinding

class TextResult : Fragment() {
    private var _binding : FragmentTextResultBinding? = null
    private val mBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTextResultBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = arguments?.getString("ExtractedText", "")
        mBinding.TextResultText.setText(text)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}