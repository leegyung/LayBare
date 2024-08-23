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
import com.google.mlkit.nl.entityextraction.EntityAnnotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ContactCreator {

    fun checkContactPermission(context: Context, launcher : ActivityResultLauncher<String>) : Boolean{
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.WRITE_CONTACTS)
            return false
        }
        return true
    }

    fun switchToContactData(extractedEntity : List<EntityAnnotation>) : HashMap<String, ArrayList<String>> {
        val result = hashMapOf<String, ArrayList<String>>()

        extractedEntity.forEach { entity ->
            val type = entity.entities.getOrNull(0)?.type
            val key = when (type) {
                8 -> "NUMBER"
                3 -> "EMAIL"
                else -> null
            }

            key?.let {
                result.getOrPut(it) { arrayListOf() }.add(entity.annotatedText)
            }
        }

        return result
    }

    suspend fun addNewContact(name : String, number : String, email : String, profileImg : String, contentResolver : ContentResolver, context : Context) : Boolean{
        val correctedNumber = number.filter { it.isDigit() }
        if(name.isEmpty()){
            return false
        }
        if(correctedNumber.isEmpty() && email.isEmpty()){
            return false
        }

        try {
            val list = ArrayList<ContentProviderOperation>()

            list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).apply {
                withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            }.build())

            list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            }.build())

            if(number.isNotEmpty()){
                list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                    withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, correctedNumber)
                    withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                }.build())
            }

            if(email.isNotEmpty()){
                list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).apply {
                    withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                    withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                }.build())
            }

            if(profileImg.isNotEmpty()){

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


            contentResolver.applyBatch(ContactsContract.AUTHORITY, list)
            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}