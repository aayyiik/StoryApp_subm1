package com.ari.submission.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.ari.submission.storyapp.R

class PasswordCustom : AppCompatEditText {
    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs:AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int): super(context, attrs, defStyleAttrs){
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                error = if (p0!!.isNotEmpty() && p0.toString().length < 6) context.getString(R.string.alert_password_less_than_6) else null
            }

        })
    }
}