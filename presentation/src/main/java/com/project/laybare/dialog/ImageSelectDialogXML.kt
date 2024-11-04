package com.project.laybare.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.laybare.R


interface ImageSelectDialogListener{
    fun onAlbumClicked()
    fun onCameraClicked()
}


class ImageSelectDialogXML : BottomSheetDialogFragment() {

    private var mListener: ImageSelectDialogListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_dialog_image_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.ImageSelectDialogAlbum).setOnClickListener {
            mListener?.onAlbumClicked()
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ImageSelectDialogCamera).setOnClickListener {
            mListener?.onCameraClicked()
            dismiss()
        }


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    fun setImageSelectDialogListener(listener : ImageSelectDialogListener){
        mListener = listener
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}