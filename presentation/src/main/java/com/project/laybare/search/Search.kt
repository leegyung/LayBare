package com.project.laybare.search

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.laybare.R
import com.project.laybare.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel : SearchViewModel by viewModels()
    private lateinit var mNavController: NavController
    private var mListListener : SearchAdapterInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = findNavController()

        initListener()
        initUI()

    }


    private fun initListener() {
        // 검색어 edittext 엔터키 리스너
        mBinding.SearchEditText.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                mViewModel.getNewKeyword()
                hideKeyboard(v)
            }
            false
        }

        // 검색어 edittext 텍스트 입력 리스너
        mBinding.SearchEditText.addTextChangedListener {
            val str = it.toString()
            mViewModel.setKeyword(str)
            mBinding.SearchDeleteTxtBtn.isVisible = str.isNotEmpty()
        }

        // 검색어 초기화 버튼 리스너
        mBinding.SearchDeleteTxtBtn.setOnClickListener {
            mBinding.SearchEditText.setText("")
            mViewModel.setKeyword("")
        }

        // 리사이클러뷰 스크롤 리스너
        mBinding.SearchResultList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && newState == 0){
                    mViewModel.getNextPage()
                }
            }
        })

        // 검색 결과 목록 리스너
        mListListener = object : SearchAdapterInterface {
            override fun onImageClicked(view: ImageView, url: String, thumbnail: String) {
                val bundle = bundleOf("imageUrl" to url, "thumbnail" to thumbnail)
                val navOption = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_up)
                    .setPopExitAnim(R.anim.slide_down)
                    .build()

                mNavController.navigate(R.id.imageDetail, bundle, navOption)
            }
        }


    }
    
    private fun initUI() {
        val keyword = mViewModel.getKeyword()
        mBinding.SearchEditText.setText(keyword)
        mBinding.SearchDeleteTxtBtn.isVisible = keyword.isNotBlank()

        mBinding.SearchResultList.apply {
            animation = null
            setHasFixedSize(true)
            if(itemDecorationCount == 0){
                addItemDecoration(SearchAdapterDecorator(resources.getDimensionPixelSize(R.dimen.dp_10)))
            }

            adapter = mViewModel.getAdapter(mListListener)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}