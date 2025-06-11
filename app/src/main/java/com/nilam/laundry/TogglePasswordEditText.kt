package com.nilam.laundry.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.nilam.laundry.R

class TogglePasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isPasswordVisible = false
    private val visibilityIcon: Drawable?
    private val visibilityOffIcon: Drawable?

    init {
        typeface = ResourcesCompat.getFont(context, R.font.poppinssemibold)
        visibilityIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility)
        visibilityOffIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off)

        // Set icon awal
        setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityIcon, null)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Setup touch listener
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawables[2]
                drawableEnd?.let {
                    val bounds = it.bounds
                    val clickAreaStart = right - bounds.width() - paddingEnd
                    if (event.rawX >= clickAreaStart) {
                        togglePasswordVisibility()
                        performClick()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        val icon = if (isPasswordVisible) visibilityOffIcon else visibilityIcon
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
        setSelection(text?.length ?: 0)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
