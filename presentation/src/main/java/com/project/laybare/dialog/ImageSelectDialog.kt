package com.project.laybare.dialog

import android.app.Dialog
import android.content.Context
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


class ImageSelectDialog : BottomSheetDialogFragment() {

    private var listener: ImageSelectDialogListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            parentFragment as? ImageSelectDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement BottomSheetDialogListener")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottom_dialog_image_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.ImageSelectDialogAlbum).setOnClickListener {
            listener?.onAlbumClicked()
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ImageSelectDialogCamera).setOnClickListener {
            listener?.onCameraClicked()
            dismiss()
        }


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}