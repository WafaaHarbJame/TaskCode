package com.interview.task.activities

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.franmontiel.localechanger.LocaleChanger
import com.jaeger.library.StatusBarUtil


open class ActivityBase : AppCompatActivity() {


    override fun onStart() {
        super.onStart()

    }


    protected fun getActiviy(): Activity {
        return this
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        } catch (e: Exception) { //            e.printStackTrace();
        }
    }





    fun Toast(vararg msg: String?) {
        var msgs: String? = ""
        for (s in msg) {
            msgs += s
        }
        Toast.makeText(getActiviy(), msgs, Toast.LENGTH_SHORT).show()
    }


    fun Toast(resId: Int) {
        Toast.makeText(getActiviy(), getString(resId), Toast.LENGTH_SHORT).show()
    }


}