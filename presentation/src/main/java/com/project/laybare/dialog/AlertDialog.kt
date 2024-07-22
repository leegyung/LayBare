package com.project.laybare.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.project.laybare.R


class AlertDialog(private val mContext : Context, private val mDeviceWidth : Int?) {
    private lateinit var dialog : Dialog
    private var itemClickListener : AlertDialogClickListener? = null


    fun createDialog(content : String, ok : String, cancel : String?) : Dialog {
        dialog = Dialog(mContext).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
            setContentView(R.layout.dialog_alert)
        }


        mDeviceWidth?.let{
            // 다이얼로그 너비 설정
            dialog.window?.setLayout(getDialogWidth(), WindowManager.LayoutParams.WRAP_CONTENT)
        }

        val contentText = dialog.findViewById<TextView>(R.id.AlertDialogContent)
        val okBtn = dialog.findViewById<Button>(R.id.AlertDialogOK)
        val cancelBtn = dialog.findViewById<Button>(R.id.AlertDialogCancel)


        contentText.text = content

        setOkBtn(ok, okBtn)
        setCancelBtn(cancel, cancelBtn)



        return dialog
    }

    private fun setOkBtn(ok : String, btn : Button) {
        btn.text = ok
        btn.setOnClickListener {
            itemClickListener?.onClickOk()
        }
    }

    private fun setCancelBtn(cancel : String?, btn : Button) {
        if(cancel == null){
            btn.isVisible = false
        }else{
            btn.apply {
                isVisible = true
                text = cancel
            }

            btn.setOnClickListener {
                itemClickListener?.onClickCancel()
            }
        }
    }





    /**
     * 마진(dp) 값을 뺀 디바이스의 너비를 pixel 값으로 리턴
     */
    private fun getDialogWidth(): Int {
        val density = mContext.resources.displayMetrics.density
        return mDeviceWidth!! - (40f * density).toInt()
    }


    interface AlertDialogClickListener {
        fun onClickOk()
        fun onClickCancel()
    }

    fun setItemClickListener(itemClickListener: AlertDialogClickListener) {
        this.itemClickListener = itemClickListener
    }





}