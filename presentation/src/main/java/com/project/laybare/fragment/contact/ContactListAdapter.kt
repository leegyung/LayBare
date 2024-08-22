package com.project.laybare.fragment.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.laybare.R
import com.project.laybare.databinding.ContactViewBinding


interface ContactListListListener{
    fun onContactSelected(contact : String)
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
                    if(!it.isSelected){
                        mListener?.onContactSelected(it.text)
                    }
                }
            }
        }

        fun bind(contact : ContactListData) {
            mBinding.ContactListText.text = contact.text
            val isSelected = contact.isSelected
            setSelectedUI(isSelected)
        }

        private fun setSelectedUI(isSelected : Boolean){
            if(isSelected){
                mBinding.ContactListText.setBackgroundResource(R.drawable.box_contact_selected)
                mBinding.ContactListText.setTextColor(itemView.context.getColor(R.color.textBlack))
            }else{
                mBinding.ContactListText.setBackgroundResource(R.drawable.box_gray_6dp)
                mBinding.ContactListText.setTextColor(itemView.context.getColor(R.color.gray30))
            }
        }


    }


}