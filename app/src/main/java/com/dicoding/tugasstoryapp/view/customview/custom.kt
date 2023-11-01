package com.dicoding.tugasstoryapp.view.customview


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.tugasstoryapp.R

class custom: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = ContextCompat.getDrawable(context, R.drawable.coustom) as Drawable
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { s, _, _, _ ->
            if (id == R.id.ed_login_email) {
                // Check if it's an email format
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    error = "Format email tidak valid"
                } else {
                    error = null
                }
            } else if (id == R.id.login_password) {
                // Check if it's more than 8 characters and contains at least one symbol
                if (s.toString().length < 8 || !s.toString().contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]"))) {
                    error = "Password harus lebih dari 8 karakter dan mengandung setidaknya 1 simbol"
                } else {
                    error = null
                }
            }
        })
    }
}