package com.project.laybare.util

import android.Manifest
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ContactCreator {

    /**
     * 연락터 write 권한 체크
     * 권한이 없다면 ActivityResultLauncher 를 통해 요청
     */
    fun checkContactPermission(context: Context, launcher : ActivityResultLauncher<String>) : Boolean{
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.WRITE_CONTACTS)
            return false
        }
        return true
    }

    /**
     * 전달 받은 프로필 데이터로 연락처 생성
     * @return Boolean: 연락터 등록 성공 여부
     */
    suspend fun addNewContact(name : String, number : String, email : String, profileImg : String, contentResolver : ContentResolver, context : Context) : Boolean{
        try {
            val list = ArrayList<ContentProviderOperation>()

            list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).apply {
                withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            }.build())

            // 이름 설정
            list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            }.build())

            // 번호가 있다면 설정
            if(number.isNotEmpty()){
                list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                    withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                }.build())
            }

            // 이메일이 있다면 추가
            if(email.isNotEmpty()){
                list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                    withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                }.build())
            }

            // 프로필 사진이 있다면 추가
            if(profileImg.isNotEmpty()){
                // 프로필 uri를 bitmap 으로 가져와 byteArray로 변환
                val profileBitmap = withContext(Dispatchers.IO) {
                        Glide.with(context).asBitmap().override(500,500).load(profileImg).submit().get()
                    }
                val stream = ByteArrayOutputStream()
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArrayImage = stream.toByteArray()

                list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                    withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArrayImage)
                }.build())
            }

            // 연락처 추가
            contentResolver.applyBatch(ContactsContract.AUTHORITY, list)
            return true

        } catch (_: Exception) {
            return false
        }
    }


}