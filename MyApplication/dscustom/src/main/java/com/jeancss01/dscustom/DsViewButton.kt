package com.jeancss01.dscustom

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet

class DsViewButton : LinearLayout {
    private var bgColor = 0
    private var disableColor = 0
    private var focusColor = -0x333334
    private var shape = DrawableHelper.SHAPE_RECTANGLE
    private var borderWidth = 0
    private var borderColor = Color.TRANSPARENT
    private var radius = 0f

    constructor(context: Context) : super(context) {
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        processAttributes(context, attrs)
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        processAttributes(context, attrs)
        initializeView()
    }

    private fun processAttributes(context: Context, attrs: AttributeSet?) {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
			initDefaultAttributes(attrs);
		else
			initDefaultAttributes17(attrs);*/
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.DsViewButton, 0, 0)
        initAttributes(attrsArray)
        attrsArray.recycle()
    }

    private fun initAttributes(attrs: TypedArray) {
        bgColor = attrs.getColor(R.styleable.DsViewButton_ds_backgroundColor, bgColor)
        disableColor = attrs.getColor(R.styleable.DsViewButton_ds_disableColor, disableColor)
        focusColor = attrs.getColor(R.styleable.DsViewButton_ds_focusColor, focusColor)
        shape = attrs.getInt(R.styleable.DsViewButton_ds_shape, shape)
        borderWidth =
            attrs.getDimensionPixelSize(R.styleable.DsViewButton_ds_borderWidth, borderWidth)
        borderColor = attrs.getInt(R.styleable.DsViewButton_ds_borderColor, borderColor)
        radius = attrs.getDimension(R.styleable.DsViewButton_ds_radius, radius)
    }

    private fun initializeView() {
        isClickable = true

        // setup background
        val helper = DrawableHelper.Builder()
            .setBackgroundColor(bgColor)
            .setFocusColor(focusColor)
            .setShape(shape)
            .setDisabledColor(disableColor)
            .setBorderWidth(borderWidth)
            .setBorderColor(borderColor)
            .setRadius(radius.toInt())
            .build()
        helper.setBackground(this, true)
    }
}