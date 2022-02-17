package com.jeancss01.dscustom


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton


class DsCustomLoadingButton : AppCompatButton, OnTouchListener {
    private var isShadowColorDefined = false

    //Custom values
    private var isShadowEnabled = true
    private var isCircularButton = false
    private var mButtonColor = 0
    private var mloaderColor = 0
    private var mShadowColor = 0
    private var mShadowHeight = 0
    private var mCornerRadius = 0
    private var isLoading = false

    //Native values
    private var mPaddingLeft = 0
    private var mPaddingRight = 0
    private var mPaddingTop = 0
    private var mPaddingBottom = 0

    //Background drawable
    private var pressedDrawable: Drawable? = null
    private var unpressedDrawable: Drawable? = null
    private var mAnimatedDrawable: CircularAnimatedDrawable? = null
    private var mPaddingProgress = 15
    private var mStrokeWidth = 10
    private val mMaxProgress = 100
    private var mColorIndicator = 0
    private val mProgress = 100
    var buttonText = ""
    var drawableIcon: Drawable? = null
    private var mcanvas: Canvas? = null

    private val indxOne = 1

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context) : super(context) {
        init()
        setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
        parseAttrs(context, attrs)
        setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        parseAttrs(context, attrs)
        setOnTouchListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        refresh()
    }

    private fun init() {
        //Init default values
        isShadowEnabled = true
        val resources = resources ?: return
        mloaderColor = resources.getColor(R.color.white)
        mButtonColor = resources.getColor(R.color.fbutton_default_color)
        mShadowColor = resources.getColor(R.color.fbutton_default_shadow_color)
        mShadowHeight = resources.getDimensionPixelSize(R.dimen.fbutton_default_shadow_height)
        mCornerRadius = resources.getDimensionPixelSize(R.dimen.fbutton_default_conner_radius)
        buttonText = text.toString()
    }

    private fun parseAttrs(context: Context, attrs: AttributeSet?) {
        //Load from custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
            ?: return
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.LoadingButton_lb_isShadowEnable -> {
                    isShadowEnabled = typedArray.getBoolean(attr, true) //Default is true
                }
                R.styleable.LoadingButton_lb_buttonColor -> {
                    mButtonColor =
                        typedArray.getColor(attr, resources.getColor(R.color.unpressed_color))
                }
                R.styleable.LoadingButton_lb_loaderColor -> {
                    mloaderColor = typedArray.getColor(attr, resources.getColor(R.color.white))
                }
                R.styleable.LoadingButton_lb_shadowColor -> {
                    mShadowColor = typedArray.getColor(attr, resources.getColor(R.color.pressed_color))
                    isShadowColorDefined = true
                }
                R.styleable.LoadingButton_lb_shadowHeight -> {
                    mShadowHeight =
                        typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_shadow_height)
                }
                R.styleable.LoadingButton_lb_cornerRadius -> {
                    mCornerRadius =
                        typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_conner_radius)
                }
                R.styleable.LoadingButton_lb_isCircular -> {
                    isCircularButton = typedArray.getBoolean(attr, false)
                }
                R.styleable.LoadingButton_lb_isLoading -> {
                    isLoading = typedArray.getBoolean(attr, false)
                }
                R.styleable.LoadingButton_lb_loaderMargin -> {
                    mCornerRadius =
                        typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_progress_margin)
                    mPaddingProgress = mCornerRadius
                }
                R.styleable.LoadingButton_lb_loaderWidth -> {
                    mCornerRadius =
                        typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_progress_width)
                    mStrokeWidth = mCornerRadius
                }
                R.styleable.LoadingButton_lb_drawableIconStart -> {
                    drawableIcon = typedArray.getDrawable(attr)
                }
            }
        }
        typedArray.recycle()

        //Get paddingLeft, paddingRight
        val attrsArray = intArrayOf(
            android.R.attr.paddingLeft,  // 0
            android.R.attr.paddingRight
        )
        val ta = context.obtainStyledAttributes(attrs, attrsArray) ?: return
        mPaddingLeft = ta.getDimensionPixelSize(0, 0)
        mPaddingRight = ta.getDimensionPixelSize(indxOne, 0)
        ta.recycle()

        //Get paddingTop, paddingBottom
        val attrsArray2 = intArrayOf(
            android.R.attr.paddingTop,  // 0
            android.R.attr.paddingBottom
        )
        val ta1 = context.obtainStyledAttributes(attrs, attrsArray2) ?: return
        mPaddingTop = ta1.getDimensionPixelSize(0, 0)
        mPaddingBottom = ta1.getDimensionPixelSize(indxOne, 0)
        ta1.recycle()

        if (drawableIcon != null) {
            drawableIcon?.let {
                setColorFilter(it, mloaderColor)
            }
        }
    }

    fun setColorFilter(drawable: Drawable, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun refresh() {
        val alpha = Color.alpha(mButtonColor)
        val hsv = FloatArray(3)
        Color.colorToHSV(mButtonColor, hsv)
        hsv[2] *= 0.8f // value component

        //if shadow color was not defined, generate shadow color = 80% brightness
        if (!isShadowColorDefined) {
            mShadowColor = Color.HSVToColor(alpha, hsv)
        }
        //Create pressed background and unpressed background drawables
        if (this.isEnabled) {
            if (isShadowEnabled) {
                pressedDrawable = createDrawable(mCornerRadius, Color.TRANSPARENT, mButtonColor)
                unpressedDrawable = createDrawable(mCornerRadius, mButtonColor, mShadowColor)
            } else {
                mShadowHeight = 0
                pressedDrawable = createDrawable(mCornerRadius, mShadowColor, Color.RED)
                unpressedDrawable = createDrawable(mCornerRadius, mButtonColor, Color.GREEN)
            }
        } else {
            Color.colorToHSV(mButtonColor, hsv)
            hsv[1] *= 0.60f // saturation component
            mShadowColor = Color.HSVToColor(alpha, hsv)
            val disabledColor = mShadowColor
            // Disabled button does not have shadow
            pressedDrawable = createDrawable(mCornerRadius, disabledColor, Color.RED)
            unpressedDrawable = createDrawable(mCornerRadius, disabledColor, Color.GREEN)
        }
        updateBackground(unpressedDrawable)
        //Set padding
        setPadding(
            mPaddingLeft,
            mPaddingTop + mShadowHeight,
            mPaddingRight,
            mPaddingBottom + mShadowHeight
        )
    }

    private fun updateBackground(background: Drawable?) {
        if (background == null) return
        //Set button background
        this.background = background
    }

    private fun createDrawable(radius: Int, topColor: Int, bottomColor: Int): LayerDrawable {
        val outerRadius = floatArrayOf(
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat()
        )

        //Top
        val topRoundRect = RoundRectShape(outerRadius, null, null)
        val topShapeDrawable = ShapeDrawable(topRoundRect)
        topShapeDrawable.paint.color = topColor
        //Bottom
        val roundRectShape = RoundRectShape(outerRadius, null, null)
        val bottomShapeDrawable = ShapeDrawable(roundRectShape)
        bottomShapeDrawable.paint.color = bottomColor
        //Create array
        val drawArray = arrayOf<Drawable>(bottomShapeDrawable, topShapeDrawable)
        val layerDrawable = LayerDrawable(drawArray)


        //Set shadow height
        if (isShadowEnabled && topColor != Color.TRANSPARENT) {
            //unpressed drawable
            layerDrawable.setLayerInset(0, 0, 0, 0, 0) /*index, left, top, right, bottom*/
        } else {
            //pressed drawable
            layerDrawable.setLayerInset(
                0,
                0,
                mShadowHeight,
                0,
                0
            ) /*index, left, top, right, bottom*/
        }
        layerDrawable.setLayerInset(1, 0, 0, 0, mShadowHeight) /*index, left, top, right, bottom*/
        return layerDrawable
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        refresh()
    }

    //Getter
    fun isShadowEnabled(): Boolean {
        return isShadowEnabled
    }

    //Setter
    fun setShadowEnabled(isShadowEnabled: Boolean) {
        this.isShadowEnabled = isShadowEnabled
        refresh()
    }

    var buttonColor: Int
        get() = mButtonColor
        set(buttonColor) {
            mButtonColor = buttonColor
            refresh()
        }

    override fun getShadowColor(): Int {
        return mShadowColor
    }

    fun setShadowColor(shadowColor: Int) {
        mShadowColor = shadowColor
        isShadowColorDefined = true
        refresh()
    }

    var shadowHeight: Int
        get() = mShadowHeight
        set(shadowHeight) {
            mShadowHeight = shadowHeight
            refresh()
        }

    fun setCornerRadius(cornerRadius: Int) {
        mCornerRadius = cornerRadius
        refresh()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                updateBackground(pressedDrawable)
                setPadding(mPaddingLeft, mPaddingTop + mShadowHeight, mPaddingRight, mPaddingBottom)
            }
            MotionEvent.ACTION_MOVE -> {
                val r = Rect()
                view.getLocalVisibleRect(r)
                if (!r.contains(motionEvent.x.toInt(), motionEvent.y.toInt() + 3 * mShadowHeight) &&
                    !r.contains(motionEvent.x.toInt(), motionEvent.y.toInt() - 3 * mShadowHeight)
                ) {
                    updateBackground(unpressedDrawable)
                    setPadding(
                        mPaddingLeft,
                        mPaddingTop + mShadowHeight,
                        mPaddingRight,
                        mPaddingBottom + mShadowHeight
                    )
                }
            }
            MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                updateBackground(unpressedDrawable)
                setPadding(
                    mPaddingLeft,
                    mPaddingTop + mShadowHeight,
                    mPaddingRight,
                    mPaddingBottom + mShadowHeight
                )
            }
        }
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (isCircularButton) {
            mCornerRadius = widthSize / 2
            refresh()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mcanvas = canvas
        if (isLoading) {
            drawIndeterminateProgress(canvas)
            text = ""
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        } else {
            if (buttonText.isNotEmpty()){
                text = buttonText
            }

            if (drawableIcon != null) {
                setCompoundDrawablesWithIntrinsicBounds(drawableIcon, null, null, null)
                setPadding(170, 0, 0, 0)
                compoundDrawablePadding = -160
            }

        }
    }

    private fun drawIndeterminateProgress(canvas: Canvas?) {
        if (mAnimatedDrawable == null) {
            val offset = (width - height) / 2
            //mColorIndicator = getResources().getColor(R.color.white);
            mColorIndicator = mloaderColor
            mAnimatedDrawable = CircularAnimatedDrawable(mColorIndicator, mStrokeWidth.toFloat())
            val left = offset + mPaddingProgress
            val right = width - offset - mPaddingProgress
            val bottom = height - mPaddingProgress
            val top = mPaddingProgress
            mAnimatedDrawable?.setBounds(left, top, right, bottom)
            mAnimatedDrawable?.callback = this
            mAnimatedDrawable?.start()
        } else {
            canvas?.let { mAnimatedDrawable?.draw(it) }
        }
    }

    private fun setLoading(loading: Boolean) {
        isLoading = loading
        if (isLoading) {
            drawIndeterminateProgress(mcanvas)
            text = ""
        } else {
            if (buttonText.isNotEmpty()) text = buttonText
        }
    }

    fun showLoading() {
        setLoading(true)
    }

    fun hideLoading() {
        setLoading(false)
    }
}