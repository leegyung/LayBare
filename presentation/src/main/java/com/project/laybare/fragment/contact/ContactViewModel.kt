package com.project.laybare.fragment.contact

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.laybare.ssot.ImageDetailData
import com.project.laybare.util.ContactCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor() : ViewModel() {
    private val _progressVisibility = MutableSharedFlow<Boolean>()
    private val _createSnackBar = MutableSharedFlow<String>()
    private val _createDialog = MutableSharedFlow<String>()

    val mProgressVisibility = _progressVisibility.asSharedFlow()
    val mCreateSnackBar = _createSnackBar.asSharedFlow()
    val mCreateDialog = _createDialog.asSharedFlow()


    private var mImageUrl = ""
    private val mNumberList = arrayListOf<ContactListData>()
    private val mEmailList = arrayListOf<ContactListData>()
    private val mNumberAdapter = ContactListAdapter(mNumberList)
    private val mEmailAdapter = ContactListAdapter(mEmailList)

    private var mName = ""
    private var mSelectedNumber = ""
    private var mSelectedEmail = ""
    private var mProfileImage = ""

    init{
        mImageUrl = ImageDetailData.getImageUrl()
        val contactList = ImageDetailData.getContactList()
        contactList?.get("NUMBER")?.forEachIndexed { index, text ->
            val select = index == 0
            if(select){
                mSelectedNumber = text
            }
            mNumberList.add(ContactListData(isSelected = select, text = text))
        }

        contactList?.get("EMAIL")?.forEachIndexed { index, text ->
            val select = index == 0
            if(select){
                mSelectedEmail = text
            }
            mEmailList.add(ContactListData(isSelected = select, text = text))
        }
    }

    fun getImageUrl() : String {
        return mImageUrl
    }

    fun getNumberAdapter() : ContactListAdapter {
        return mNumberAdapter
    }

    fun getEmailAdapter() : ContactListAdapter {
        return mEmailAdapter
    }

    fun getEditedName() : String {
        return mName
    }

    fun setEditedName(name : String){
        mName = name
    }

    fun setProfileImage(uri : String) {
        mProfileImage = uri
    }

    fun getProfileImage() : String {
        return mProfileImage
    }

    @SuppressLint("NotifyDataSetChanged")
    fun contactSelected(type : String, contact : String) {
        if(type == "number"){
            mSelectedNumber = contact
            mNumberList.forEach {
                it.isSelected = it.text == contact
            }
            mNumberAdapter.notifyDataSetChanged()
        }else{
            mSelectedEmail = contact
            mEmailList.forEach {
                it.isSelected = it.text == contact
            }
        }
    }

    fun addContact(contentResolver : ContentResolver, context: Context) {
        viewModelScope.launch {
            if(mName.isEmpty()) {
                _createSnackBar.emit("연락처 이름을 입력 해 주세요")
                return@launch
            }

            val formattedNumber = mSelectedNumber.filter { it.isDigit() }

            if(formattedNumber.isEmpty() && mSelectedEmail.isEmpty()) {
                _createSnackBar.emit("선택된 번호/이메일이 없어요...")
                return@launch
            }

            _progressVisibility.emit(true)
            val contactCreated = ContactCreator().addNewContact(mName, formattedNumber, mSelectedEmail, mProfileImage, contentResolver, context)
            if(contactCreated){
                _createDialog.emit("연락처 추가 완료")
            }else{
                _createSnackBar.emit("연락처 추가 오류")
            }
            _progressVisibility.emit(false)
        }
    }

}