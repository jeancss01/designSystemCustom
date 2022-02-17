package com.jeancss01.dscustom

import android.widget.LinearLayout
import android.view.Gravity
import android.graphics.Typeface
import android.widget.TextView
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.content.res.TypedArray
import androidx.annotation.RequiresApi
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

class DsCustomButton : LinearLayout {
    private var ctx: Context? = null
    private var borderColor = Color.TRANSPARENT
    private var borderWidth = 0
    private var radius = 0f
    private var isEnabledCustom = true
    private var backgroundColor = Color.parseColor("#D6D7D7")
    private var focusColor = Color.parseColor("#B0B0B0")
    private var disableColor = Color.parseColor("#D6D7D7")
    private var textSize = 37
    private var textColor = Color.parseColor("#1C1C1C")
    private var disabledTextColor = Color.parseColor("#A0A0A0")
    private var textAllCaps = false
    private var textStyle = 0
    private val fpadding = 10
    private var padding = 20
    private var paddingLeftCustom = 20
    private var paddingTopCustom = 20
    private var paddingRightCustom = 20
    private var paddingBottomCustom = 20
    private var text: String? = ""
    private val gravityCustom = Gravity.CENTER
    private var drawableResource = 0
    private var drawable: Drawable? = null
    private var fontIcon: String? = ""
    private var iconPosition = POSITION_LEFT
    private var iconColor = 0
    private var iconSize = 37
    private val fixedIconPadding = 30
    private var iconPadding = 0
    private var lGravity = 0
    private var awesomeIconTypeFace: Typeface? = null
    private var imageView: ImageView? = null
    private var textView: TextView? = null

    constructor(context: Context) : super(context) {
        this.ctx = context
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.ctx = context

        processAttributes(context, attrs)
        initializeView()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.ctx = context
        processAttributes(context, attrs)
        initializeView()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.ctx = context
        processAttributes(context, attrs)
        initializeView()
    }

    private fun initializeView() {
        if (!isInEditMode) {
            awesomeIconTypeFace = getAwesomeTypeface(context)
            //Log.e("TAG", "awesomeIconTypeFace");
        }
        if (iconPosition == POSITION_TOP || iconPosition == POSITION_BOTTOM) {
            this.orientation = VERTICAL
        } else {
            this.orientation = HORIZONTAL
        }
        if (this.layoutParams == null) {
            val containerParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            this.layoutParams = containerParams
        }
        super.setGravity(gravityCustom)
        super.setEnabled(isEnabledCustom)
        this.isClickable = isEnabledCustom
        this.isFocusable = true
        setupTextView()
        setupImageView()
        //if (imageView != null) this.addView(imageView);
        setupBackground()
        super.setPadding(paddingLeft, paddingTopCustom, paddingRightCustom, paddingBottomCustom)

        //super.setPadding(0, 0, 0, 0);
        removeAllViews()
        if (iconPosition == POSITION_RIGHT || iconPosition == POSITION_BOTTOM) {
            if (textView != null) this.addView(textView)
            if (imageView != null) this.addView(imageView)
        } else {
            if (imageView != null) this.addView(imageView)
            if (textView != null) this.addView(textView)
        }
        updateGravity()
    }

    private fun processAttributes(context: Context, attrs: AttributeSet?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) initDefaultAttributes(attrs) else initDefaultAttributes17(
            attrs
        )
        val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.DsButton, 0, 0)
        initAttributes(attrsArray)
        attrsArray.recycle()
    }

    private fun initDefaultAttributes(attrs: AttributeSet?) {
        val defAttr = intArrayOf(
            android.R.attr.gravity,
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingTop,
            android.R.attr.paddingRight,
            android.R.attr.paddingBottom
        )
        val defAttrsArray = context.obtainStyledAttributes(attrs, defAttr)
        //gravity = defAttrsArray.getInt(0, gravity);
        padding = defAttrsArray.getDimensionPixelSize(one, padding)

        // initialize padding to all
        if (padding != 0) {
            paddingBottomCustom = padding
            paddingRightCustom = paddingBottomCustom
            paddingTopCustom = paddingRightCustom
            paddingLeftCustom = paddingTopCustom
        }
        paddingLeftCustom = defAttrsArray.getDimensionPixelSize(two, paddingLeft)
        paddingTopCustom = defAttrsArray.getDimensionPixelSize(three, paddingTopCustom)
        paddingRightCustom = defAttrsArray.getDimensionPixelSize(four, paddingRightCustom)
        paddingBottomCustom = defAttrsArray.getDimensionPixelSize(five, paddingBottomCustom)
        paddingLeftCustom = defAttrsArray.getDimensionPixelSize(six, paddingLeft)
        paddingRightCustom = defAttrsArray.getDimensionPixelSize(seven, paddingRightCustom)
        defAttrsArray.recycle()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initDefaultAttributes17(attrs: AttributeSet?) {

        val defAttr = intArrayOf(
            android.R.attr.gravity,
            android.R.attr.padding,
            android.R.attr.paddingLeft,
            android.R.attr.paddingTop,
            android.R.attr.paddingRight,
            android.R.attr.paddingBottom,
            android.R.attr.paddingStart,
            android.R.attr.paddingEnd
        )
        val defAttrsArray = context.obtainStyledAttributes(attrs, defAttr)
        //gravity = defAttrsArray.getInt(0, gravity);
        padding = defAttrsArray.getDimensionPixelSize(one, padding)
        paddingBottomCustom = padding
        paddingRightCustom = paddingBottomCustom
        paddingTopCustom = paddingRightCustom
        paddingLeftCustom = paddingTopCustom
        paddingLeftCustom = defAttrsArray.getDimensionPixelSize(two, paddingLeft)
        paddingTopCustom = defAttrsArray.getDimensionPixelSize(three, paddingTopCustom)
        paddingRightCustom = defAttrsArray.getDimensionPixelSize(four, paddingRightCustom)
        paddingBottomCustom = defAttrsArray.getDimensionPixelSize(five, paddingBottomCustom)
        paddingLeftCustom = defAttrsArray.getDimensionPixelSize(six, paddingLeft)
        paddingRightCustom = defAttrsArray.getDimensionPixelSize(seven, paddingRightCustom)
        defAttrsArray.recycle()
    }

    /**
     * Initialize Attributes arrays
     *
     * @param attrs : Attributes array
     */
    private fun initAttributes(attrs: TypedArray) {
        radius = attrs.getDimension(R.styleable.DsButton_ds_radius, radius)
        borderColor = attrs.getColor(R.styleable.DsButton_ds_borderColor, borderColor)
        borderWidth =
            attrs.getDimension(R.styleable.DsButton_ds_borderWidth, borderWidth.toFloat())
                .toInt()
        backgroundColor = attrs.getColor(R.styleable.DsButton_ds_backgroundColor, backgroundColor)
        disableColor = attrs.getColor(R.styleable.DsButton_ds_disableColor, disableColor)
        focusColor = attrs.getColor(R.styleable.DsButton_ds_focusColor, focusColor)
        text = attrs.getString(R.styleable.DsButton_ds_text)
        textColor = attrs.getColor(R.styleable.DsButton_ds_textColor, textColor)
        disabledTextColor =
            attrs.getColor(R.styleable.DsButton_ds_disabledTextColor, disabledTextColor)
        textSize = attrs.getDimensionPixelSize(R.styleable.DsButton_ds_textSize, textSize)
        textStyle = attrs.getInt(R.styleable.DsButton_ds_textStyle, textStyle)
        textAllCaps = attrs.getBoolean(R.styleable.DsButton_ds_textAllCaps, textAllCaps)
        fontIcon = attrs.getString(R.styleable.DsButton_ds_fontIcon)
        iconSize = attrs.getDimensionPixelSize(R.styleable.DsButton_ds_iconSize, iconSize)
        iconColor = attrs.getColor(R.styleable.DsButton_ds_iconColor, iconColor)
        iconPosition = attrs.getInt(R.styleable.DsButton_ds_iconPosition, iconPosition)
        drawableResource =
            attrs.getResourceId(R.styleable.DsButton_ds_drawableResource, drawableResource)
        iconPadding =
            attrs.getDimensionPixelSize(R.styleable.DsButton_ds_iconPadding, iconPadding)
        lGravity = attrs.getInt(R.styleable.DsButton_ds_gravity, lGravity)
        isEnabledCustom = attrs.getBoolean(R.styleable.DsButton_ds_enabled, isEnabledCustom)
    }

    private fun setupBackground() {


        // Default Drawable
        val defaultDrawable = GradientDrawable()
        defaultDrawable.cornerRadius = radius
        defaultDrawable.setColor(backgroundColor)

        //Focus Drawable
        val focusDrawable = GradientDrawable()
        focusDrawable.cornerRadius = radius
        focusDrawable.setColor(focusColor)

        // Disabled Drawable
        val disabledDrawable = GradientDrawable()
        disabledDrawable.cornerRadius = radius
        disabledDrawable.setColor(disableColor)
        //disabledDrawable.setStroke(mBorderWidth, mDisabledBorderColor);
        if (borderColor != 0 && borderWidth > 0) {
            defaultDrawable.setStroke(borderWidth, borderColor)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = getRippleDrawable(defaultDrawable, focusDrawable, disabledDrawable)
        } else {
            val states = StateListDrawable()

            // Focus/Pressed Drawable
            val drawable2 = GradientDrawable()
            drawable2.cornerRadius = radius
            drawable2.setColor(focusColor)
            if (focusColor != 0) {
                states.addState(intArrayOf(android.R.attr.state_pressed), drawable2)
                states.addState(intArrayOf(android.R.attr.state_focused), drawable2)
                states.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
            }
            states.addState(intArrayOf(), defaultDrawable)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(states)
            } else {
                this.background = states
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getRippleDrawable(
        defaultDrawable: Drawable,
        focusDrawable: Drawable,
        disabledDrawable: Drawable
    ): Drawable {
        return if (!isEnabled) {
            disabledDrawable
        } else {
            RippleDrawable(ColorStateList.valueOf(focusColor), defaultDrawable, focusDrawable)
        }
    }

    private fun setupTextView() {
        textView = TextView(context)
        //textView.setBackgroundColor(Color.BLUE);
        val textViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView?.layoutParams = textViewParams

        /*if (text == null || text.isEmpty()) {
			text = "";
			return;
		}*/textView?.text = text
        textView?.setTextColor(if (isEnabledCustom) textColor else disabledTextColor)
        textView?.textSize = pxToSp(context, textSize.toFloat()).toFloat()
        textView?.isAllCaps = textAllCaps

        // fonts style normal, bold, italic
        when (textStyle) {
            2 -> {
                textView?.setTypeface(textView?.typeface, Typeface.ITALIC)
            }
            1 -> {
                textView?.setTypeface(textView?.typeface, Typeface.BOLD)
            }
            else -> {
                textView?.setTypeface(textView?.typeface, Typeface.NORMAL)
            }
        }

        //textView.setEnabled(isEnabled());
        textView?.gravity = gravityCustom
    }

    private fun setupImageView() {
        imageView = ImageView(context)

        /*if (drawableResource == 0 && (fontIcon == null || fontIcon.isEmpty())) {
			return;
		}*/

        //imageView = new ImageView(context);

        // change iconColor to textColor if user not defined
        if (iconColor == 0) {
            iconColor = textColor
        }

        // add font_awesome icon to imageView
        if (fontIcon != null) {
            fontIcon?.let {
                if (it.isNotEmpty()) {
                    val color = if (isEnabled) iconColor else disabledTextColor
                    imageView?.setImageBitmap(textToBitmap(it, iconSize.toFloat(), color))
                }
            }
        }

        // add drawable icon to imageview
        if (drawableResource != 0) {
            imageView?.setImageResource(drawableResource)
        }
        if (drawable != null) {
            imageView?.setImageDrawable(drawable)
        }
        updateIconPadding()

        // icon padding
        /*LayoutParams imageViewParams;
		if (iconPosition == POSITION_TOP || iconPosition == POSITION_BOTTOM) {
			imageViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			imageViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}*/

        /*if (textView != null && imageView != null) {
			if (iconPosition == POSITION_LEFT) {
				imageViewParams.setMargins(0, 0, getDrawablePadding(), 0);
			} else if (iconPosition == POSITION_TOP) {
				imageViewParams.setMargins(0, 0, 0, getDrawablePadding());
			} else if (iconPosition == POSITION_RIGHT) {
				imageViewParams.setMargins(getDrawablePadding(), 0, 0, 0);
			} else if (iconPosition == POSITION_BOTTOM) {
				imageViewParams.setMargins(0, getDrawablePadding(), 0, 0);
			}
		}*/


        //imageView.setEnabled(isEnabled());

        //imageView.setLayoutParams(imageViewParams);
        //imageView.setBackgroundColor(Color.RED);
    }

    /*public static int spToPx(final Context context, final float sp) {
		return Math.round(sp * context.getResources().getDisplayMetrics().scaledDensity);
	}
	public static int pxToDp(final Context context, final float px) {
		return Math.round(px / context.getResources().getDisplayMetrics().density);
	}
	public static int dpToPx(final Context context, final float dp) {
		return Math.round(dp * context.getResources().getDisplayMetrics().density);
	}*/
    private fun updateGravity() {
        if (lGravity == GRAVITY_CENTER) {
            // center
            super.setGravity(Gravity.CENTER)
        } else if (lGravity == GRAVITY_LEFT) {
            // left
            super.setGravity(Gravity.START or Gravity.CENTER_VERTICAL)
        } else if (lGravity == GRAVITY_RIGHT) {
            // right
            super.setGravity(Gravity.END or Gravity.CENTER_VERTICAL)
        } else if (lGravity == GRAVITY_TOP) {
            // top
            super.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        } else if (lGravity == GRAVITY_BOTTOM) {
            // bottom
            super.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        }
    }

    private fun updateIconPadding() {
        val imageViewParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        when {
            fontIcon == null -> {
                imageViewParams.setMargins(0, 0, 0, 0)
            }
            fontIcon != null -> {
                fontIcon?.let {
                    if (it.isEmpty()) {
                        imageViewParams.setMargins(0, 0, 0, 0)
                    }
                }
            }
            iconPosition == POSITION_LEFT -> {
                imageViewParams.setMargins(0, 0, drawablePadding, 0)
            }
            iconPosition == POSITION_TOP -> {
                imageViewParams.setMargins(0, 0, 0, drawablePadding)
            }
            iconPosition == POSITION_RIGHT -> {
                imageViewParams.setMargins(drawablePadding, 0, 0, 0)
            }
            iconPosition == POSITION_BOTTOM -> {
                imageViewParams.setMargins(0, drawablePadding, 0, 0)
            }
        }
        imageView?.layoutParams = imageViewParams
    }

    private val drawablePadding: Int
        private get() = if (iconPadding != 0) {
            iconPadding
        } else fixedIconPadding// round

    // ascent() is negative
    private val fontBitmap: Bitmap
        private get() {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = iconColor
            if (awesomeIconTypeFace != null && !isInEditMode) {
                paint.typeface = awesomeIconTypeFace
                paint.textSize = iconSize.toFloat()
            } else {
                fontIcon = "o"
                paint.textSize = (iconSize - 15).toFloat()
            }
            paint.textAlign = Paint.Align.LEFT
            val baseline = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(fontIcon) + 0.5f).toInt() // round
            val height = (baseline + paint.descent() + 0.5f).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            fontIcon?.let { canvas.drawText(it, 0f, baseline, paint) }
            return image
        }

    private fun textToBitmap(text: String, iconSize: Float, textColor: Int): Bitmap {

        /*if (awesomeIconTypeFace == null) {
			return null;
		}*/

        //float tSize = iconSize != 0 ? iconSize : spToPx(context, this.fixedTextSize);
        var text: String? = text
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = textColor
        if (awesomeIconTypeFace != null && !isInEditMode) {
            paint.typeface = awesomeIconTypeFace
            paint.textSize = iconSize
        } else {
            text = "O"
            paint.textSize = (iconSize / 2.5).toFloat()
        }
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        text?.let { canvas.drawText(it, 0f, baseline, paint) }
        return image
    }

    var allCaps: Boolean
        get() = textAllCaps
        set(allCaps) {
            textAllCaps = allCaps
            textView?.isAllCaps = allCaps
        }

    fun setText(text: String?) {
        this.text = text
        if (textView != null) textView?.text = text else setupTextView()
    }

    fun getText(): String? {
        return text
    }

    fun setTextStyle(style: Int) {
        textStyle = style
        if (textStyle == TEXT_STYLE_ITALIC) {
            textView?.setTypeface(textView?.typeface, Typeface.ITALIC)
        } else if (textStyle == TEXT_STYLE_BOLD) {
            textView?.setTypeface(textView?.typeface, Typeface.BOLD)
        } else {
            textView?.setTypeface(textView?.typeface, Typeface.NORMAL)
        }
    }

    fun getTextStyle(): Int {
        return textStyle
    }

    fun setTextSize(size: Int) {
        textSize = size
        textView?.textSize = size.toFloat()
    }

    fun getTextSize(): Float {
        return textSize.toFloat()
    }

    fun setTextColor(color: Int) {
        textColor = color
        textView?.setTextColor(if (isEnabled) textColor else disabledTextColor)
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setBorderWidth(size: Int) {
        borderWidth = size
        setupBackground()
    }

    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        setupBackground()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setRadius(size: Float) {
        radius = size
        setupBackground()
    }

    fun getRadius(): Float {
        return radius
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        backgroundColor = color
        setupBackground()
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun setFocusColor(@ColorInt color: Int) {
        focusColor = color
        setupBackground()
    }

    fun getFocusColor(): Int {
        return focusColor
    }

    fun setDisableColor(@ColorInt color: Int) {
        disableColor = color
        setupBackground()
    }

    fun getDisableColor(): Int {
        return disableColor
    }

    override fun setEnabled(enabled: Boolean) {
        //super.setEnabled(enabled);
        isEnabled = enabled
        initializeView()
    }

    fun setDisabledTextColor(color: Int) {
        disabledTextColor = color
        initializeView()
    }

    fun getDisabledTextColor(): Int {
        return disabledTextColor
    }

    var disabledColor: Int
        get() = disableColor
        set(color) {
            disableColor = color
            setupBackground()
        }

    fun setFontIcon(fontIcon: String?) {
        this.fontIcon = fontIcon
        imageView?.setImageBitmap(fontBitmap)
    }

    fun getIconSize(): Int {
        return iconSize
    }

    fun setIconSize(iconSize: Int) {
        this.iconSize = iconSize
        imageView?.setImageBitmap(fontBitmap)
    }

    fun setIconColor(color: Int) {
        iconColor = color
        imageView?.setImageBitmap(fontBitmap)
    }

    fun setIconPosition(position: Int) {
        iconPosition = position
        initializeView()
    }

    /*public int getIconPosition() {
		return iconPosition;
	}*/
    fun setDrawableResource(@DrawableRes resource: Int) {
        drawableResource = resource
        //initializeView();
        imageView?.setImageResource(resource)
    }

    fun setDrawable(drawable: Drawable?) {
        this.drawable = drawable
        initializeView()
    }

    fun setIconPadding(padding: Int) {
        iconPadding = padding
        //updateIconPadding();
        initializeView()
    }

    fun getIconPadding(): Int {
        return iconPadding
    }

    fun setTextGravity(gravity: Int) {
        lGravity = gravity
        initializeView()
        //if (textView != null)
        //updateGravity();
        //updateIconPadding();
        /*else
			initializeView();*/
    }

    companion object {
        /**
         * icon position
         */
        const val POSITION_LEFT = 1
        const val POSITION_RIGHT = 2
        const val POSITION_TOP = 3
        const val POSITION_BOTTOM = 4
        const val GRAVITY_CENTER = 0
        const val GRAVITY_LEFT = 1
        const val GRAVITY_RIGHT = 2
        const val GRAVITY_TOP = 3
        const val GRAVITY_BOTTOM = 4
        const val TEXT_STYLE_NORMAL = 0
        const val TEXT_STYLE_BOLD = 1
        const val TEXT_STYLE_ITALIC = 2

        val one = 1
        val two = 1
        val three = 1
        val four = 4
        val five = 5
        val six = 6
        val seven = 7

        private const val FONT_AWESOME = "fonts/fontawesome-webfont.ttf"
        private val TAG = Button::class.java.simpleName
        private fun pxToSp(context: Context, px: Float): Int {
            return Math.round(px / context.resources.displayMetrics.scaledDensity)
        }

        fun getAwesomeTypeface(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, FONT_AWESOME)
        }
    }
}