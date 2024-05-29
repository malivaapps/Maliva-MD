package com.example.maliva.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.maliva.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize()
    }

    private fun initialize() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = charSequence.toString()
                validateInput(inputText)
            }

            override fun afterTextChanged(editable: Editable?) {
                val inputText = editable.toString()
                validateInput(inputText)
            }
        })
    }

    private fun validateInput(input: String) {
        val parent = parent.parent
        if (parent is TextInputLayout) {
            when (inputType) {
                EMAIL_TYPE -> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                        parent.error = context.getString(R.string.valid_email)
                        setBackgroundResource(R.drawable.stroke_error)
                    } else {
                        parent.error = null
                        setBackgroundResource(R.drawable.edit_text_background) // Change to default background
                    }
                }
                PASSWORD_TYPE -> {
                    if (input.length < 8) {
                        parent.error = context.getString(R.string.valid_password)
                        setBackgroundResource(R.drawable.stroke_error)
                    } else {
                        parent.error = null
                        setBackgroundResource(R.drawable.edit_text_background) // Change to default background
                    }
                }
            }
        }
    }


    companion object {
        const val EMAIL_TYPE = 0x00000021
        const val PASSWORD_TYPE = 0x00000081
    }
}
