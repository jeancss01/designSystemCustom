package com.jeancss01.dscustom

import android.R
import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt


class DrawableHelper protected constructor(var mBuilder: Builder) {
    class Builder {
        var radius = 0
            protected set
        var borderWidth = 0
            protected set

        @ColorInt
        var backgroundColor: Int = Color.TRANSPARENT
            protected set

        @ColorInt
        var focusColor = -0x333334
            protected set

        @ColorInt
        var disabledColor: Int = Color.TRANSPARENT
            protected set

        @ColorInt
        var borderColor = 0
            protected set
        var isEnabled = true
            protected set
        var shape = 101
            private set

        fun setRadius(radius: Int): Builder {
            this.radius = radius
            return this
        }

        fun setBorderWidth(borderWidth: Int): Builder {
            this.borderWidth = borderWidth
            return this
        }

        fun setBackgroundColor(backgroundColor: Int): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        fun setFocusColor(focusColor: Int): Builder {
            this.focusColor = focusColor
            return this
        }

        fun setDisabledColor(disabledColor: Int): Builder {
            this.disabledColor = disabledColor
            return this
        }

        fun setBorderColor(borderColor: Int): Builder {
            this.borderColor = borderColor
            return this
        }

        fun setEnabled(enabled: Boolean): Builder {
            isEnabled = enabled
            return this
        }

        fun setShape(shape: Int): Builder {
            this.shape = shape
            return this
        }

        fun build(): DrawableHelper {
            return DrawableHelper(this)
        }
    }

    private fun setupBackground(): Drawable? {
        var drawable: Drawable? = null

        // Default Drawable
        val defaultDrawable = GradientDrawable()
        defaultDrawable.cornerRadius = mBuilder.radius.toFloat()
        defaultDrawable.setColor(mBuilder.backgroundColor)
        //defaultDrawable.setShape(getShape());

        //Focus Drawable
        val focusDrawable = GradientDrawable()
        focusDrawable.cornerRadius = mBuilder.radius.toFloat()
        focusDrawable.setColor(mBuilder.focusColor)
        //focusDrawable.setShape(getShape());

        // Disabled Drawable
        val disabledDrawable = GradientDrawable()
        disabledDrawable.cornerRadius = mBuilder.radius.toFloat()
        disabledDrawable.setColor(mBuilder.disabledColor)
        //disabledDrawable.setShape(getShape());
        //disabledDrawable.setStroke(mBorderWidth, mDisabledBorderColor);
        if (mBuilder.borderColor != 0 && mBuilder.borderWidth > 0) {
            defaultDrawable.setStroke(mBuilder.borderWidth, mBuilder.borderColor)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getRippleDrawable(defaultDrawable, focusDrawable, disabledDrawable)
        } else {
            val states = StateListDrawable()

            // Focus/Pressed Drawable
            val drawable2 = GradientDrawable()
            drawable2.cornerRadius = mBuilder.radius.toFloat()
            drawable2.setColor(mBuilder.focusColor)
            if (mBuilder.focusColor != 0) {
                states.addState(intArrayOf(R.attr.state_pressed), drawable2)
                states.addState(intArrayOf(R.attr.state_focused), drawable2)
                states.addState(intArrayOf(-R.attr.state_enabled), disabledDrawable)
            }
            states.addState(intArrayOf(), defaultDrawable)
            drawable = states
            /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
				this.setBackgroundDrawable(states);
			} else {
				this.setBackground(states);
			}*/
        }
        return drawable
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getRippleDrawable(
        defaultDrawable: Drawable,
        focusDrawable: Drawable,
        disabledDrawable: Drawable
    ): Drawable {
        return if (!mBuilder.isEnabled) {
            disabledDrawable
        } else {
            RippleDrawable(
                ColorStateList.valueOf(mBuilder.focusColor),
                defaultDrawable,
                focusDrawable
            )
        }
    }

    fun setBackground(view: View) {
        val drawable = setupBackground()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable)
        } else {
            view.background = drawable
        }
    }

    fun setBackground(view: View, isClickable: Boolean) {
        var drawable: Drawable? = null
        if (isClickable) {
            drawable = setupBackground()
        } else {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.cornerRadius = mBuilder.radius.toFloat()
            gradientDrawable.setColor(mBuilder.backgroundColor)

            //Log.e("TAG", mBuilder.borderColor+"  "+ mBuilder.borderWidth);
            if (mBuilder.borderColor != 0 && mBuilder.borderWidth > 0) {
                //Log.e("TAG", "dksajf");
                gradientDrawable.setStroke(mBuilder.borderWidth, mBuilder.borderColor)
            }
            gradientDrawable.shape = shape
            drawable = gradientDrawable
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable)
        } else {
            view.background = drawable
        }
    }

    private val shape: Int
        private get() = when (mBuilder.shape) {
            SHAPE_OVAL -> GradientDrawable.OVAL
            else -> GradientDrawable.RECTANGLE
        }

    companion object {
        const val SHAPE_RECTANGLE = 101
        const val SHAPE_OVAL = 102
    }
}