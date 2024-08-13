package com.project.laybare.fragment.textResult

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var _binding : FragmentTextResultBinding? = null
    private lateinit var mContext : Context
    private val mBinding get() = _binding!!
    private lateinit var mNavController: NavController
    private val mViewModel : TextResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = requireContext()
        _binding = FragmentTextResultBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()

        if(mViewModel.requireTextData()) {
            val text = arguments?.getString("ExtractedText", "")
            if(text.isNullOrEmpty()) {
                createDialog("텍스트 정보를 받아오지 못했습니다.")
            }else{
                mViewModel.setTextResult(text)
            }
        }

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

    private fun createDialog(msg : String) {

        val width = resources.displayMetrics.widthPixels
        val constructor = AlertDialog(mContext, width)
        val dialog = constructor.createDialog(1, msg, "확인")
        dialog.setCancelable(false)

        constructor.setItemClickListener(object : AlertDialog.AlertDialogClickListener{
            override fun onClickOk() {
                dialog.dismiss()
                mNavController.popBackStack()
            }
            override fun onClickCancel() {}
        })

        dialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}