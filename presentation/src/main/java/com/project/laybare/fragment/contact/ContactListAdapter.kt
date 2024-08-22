package com.project.laybare.fragment.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.laybare.databinding.ContactViewBinding


interface ContactListListListener{
    fun onContactSelected(type : String, contact : String)
}


class ContactListAdapter(private val mContactList : ArrayList<ContactListData>) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    private var mListener : ContactListListListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ContactViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mContactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mContactList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun setAdapterListener(listener : ContactListListListener?) {
        mListener = listener
    }


    inner class ViewHolder(private val mBinding: ContactViewBinding) : RecyclerView.ViewHolder(mBinding.root) {

        init {
            mBinding.ContactListText.setOnClickListener {
                mContactList.getOrNull(bindingAdapterPosition)?.let {
                    mListener?.onContactSelected(it.type, it.text)
                }
            }
        }

        fun bind(contact : ContactListData) {
            mBinding.ContactListText.text = contact.text
        }
    }


}