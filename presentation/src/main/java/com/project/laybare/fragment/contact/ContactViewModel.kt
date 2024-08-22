package com.project.laybare.fragment.contact

import androidx.lifecycle.ViewModel
import com.project.laybare.ssot.ImageDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor() : ViewModel() {
    private var mImageUrl = ""
    private val mNumberList = arrayListOf<ContactListData>()
    private val mEmailList = arrayListOf<ContactListData>()
    private val mNumberAdapter = ContactListAdapter(mNumberList)
    private val mEmailAdapter = ContactListAdapter(mEmailList)

    init{
        mImageUrl = ImageDetailData.getImageUrl()
        val contactList = ImageDetailData.getContactList()
        contactList?.get("NUMBER")?.forEach {
            mNumberList.add(ContactListData(text = it))
        }
        contactList?.get("EMAIL")?.forEach {
            mEmailList.add(ContactListData(type = "email", text = it))
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

}