package com.tp.recyclertree

import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

class CustomGlowTextView : AppCompatTextView, OnTouchListener {
    private var mBackgroundColor = 0
    private var mGlowColor = 0
    private var mStrokeColor = 0
    private var mUnpressedGlowSize = 0
    private var mPressedGlowSize = 0
    private var mCornerRadius = 0
    private var strokeWidth = 0

    constructor(context: Context?) : super(context!!) {
        this.stateListAnimator = null
        setOnTouchListener(this)
        initDefaultValues()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.stateListAnimator = null
        setOnTouchListener(this)
        initDefaultValues()
        parseAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.stateListAnimator = null
        setOnTouchListener(this)
        initDefaultValues()
        parseAttrs(context, attrs)
    }

    override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> background = getBackgroundWithGlow(
                this,
                mBackgroundColor,
                mGlowColor,
                mCornerRadius,
                mUnpressedGlowSize,
                mPressedGlowSize,
                strokeWidth,
                mStrokeColor
            )
            MotionEvent.ACTION_UP -> background = getBackgroundWithGlow(
                this,
                mBackgroundColor,
                mGlowColor,
                mCornerRadius,
                mUnpressedGlowSize,
                mUnpressedGlowSize,
                strokeWidth,
                mStrokeColor
            )
        }
        return false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateButtonGlow()
    }

    @SuppressLint("ResourceAsColor")
    private fun parseAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlowButton)
            ?: return
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.GlowButton_buttonColor) {
                mBackgroundColor = typedArray.getColor(attr, R.color.default_background_color)
            } else if (attr == R.styleable.GlowButton_glowColor) {
                mGlowColor = typedArray.getColor(attr, R.color.default_glow_color)
            } else if (attr == R.styleable.GlowButton_strokeColor) {
                mStrokeColor = typedArray.getColor(attr, R.color.default_stroke_color)
            } else if (attr == R.styleable.GlowButton_cornerRadiusGlowButton) {
                mCornerRadius =
                    typedArray.getDimensionPixelSize(attr, R.dimen.default_corner_radius)
            } else if (attr == R.styleable.GlowButton_unpressedGlowSize) {
                mUnpressedGlowSize =
                    typedArray.getDimensionPixelSize(attr, R.dimen.default_unpressed_glow_size)
            } else if (attr == R.styleable.GlowButton_pressedGlowSize) {
                mPressedGlowSize =
                    typedArray.getDimensionPixelSize(attr, R.dimen.default_pressed_glow_size)
            } else if (attr == R.styleable.GlowButton_strokeSize) {
                strokeWidth = typedArray.getDimensionPixelSize(attr, R.dimen.default_stroke_size)
            }
        }
        typedArray.recycle()
    }

    private fun updateButtonGlow() {
        background = getBackgroundWithGlow(
            this,
            mBackgroundColor,
            mGlowColor,
            mCornerRadius,
            mUnpressedGlowSize,
            mUnpressedGlowSize,
            strokeWidth,
            mStrokeColor
        )
    }

    private fun initDefaultValues() {
        val resources = resources ?: return
        mBackgroundColor = resources.getColor(R.color.default_background_color)
        mGlowColor = resources.getColor(R.color.default_glow_color)
        mCornerRadius = resources.getDimensionPixelSize(R.dimen.default_corner_radius)
        mUnpressedGlowSize = resources.getDimensionPixelSize(R.dimen.default_unpressed_glow_size)
        mPressedGlowSize = resources.getDimensionPixelSize(R.dimen.default_pressed_glow_size)
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        mBackgroundColor = backgroundColor
        updateButtonGlow()
    }

    fun getBackgroundColor(): Int {
        return mBackgroundColor
    }

    var glowColor: Int
        get() = mGlowColor
        set(glowColor) {
            mGlowColor = glowColor
            updateButtonGlow()
        }
    var unpressedGlowSize: Int
        get() = mUnpressedGlowSize
        set(unpressedGlowSize) {
            mUnpressedGlowSize = unpressedGlowSize
            updateButtonGlow()
        }
    var pressedGlowSize: Int
        get() = mPressedGlowSize
        set(pressedGlowSize) {
            mPressedGlowSize = pressedGlowSize
            updateButtonGlow()
        }
    var cornerRadius: Int
        get() = mCornerRadius
        set(cornerRadius) {
            mCornerRadius = cornerRadius
            updateButtonGlow()
        }

    companion object {
        fun getBackgroundWithGlow(
            view: View, backgroundColor: Int,
            glowColor: Int,
            cornerRadius: Int,
            unPressedGlowSize: Int,
            pressedGlowSize: Int, strokeWidth: Int, mStrokeColor: Int
        ): Drawable {
            val outerRadius = FloatArray(8)
            Arrays.fill(outerRadius, cornerRadius.toFloat())
            val shapeDrawablePadding = Rect()
            val shapeDrawable = ShapeDrawable()
            shapeDrawable.paint.color = backgroundColor
            shapeDrawable.shape = RoundRectShape(outerRadius, null, null)
            val shapeStroke = ShapeDrawable()
            shapeStroke.paint.color = mStrokeColor
            shapeStroke.setPadding(shapeDrawablePadding)
            shapeStroke.paint.setShadowLayer(pressedGlowSize.toFloat(), 0f, 0f, glowColor)
            shapeStroke.shape = RoundRectShape(outerRadius, null, null)
            view.setLayerType(LAYER_TYPE_SOFTWARE, shapeStroke.paint)
            val drawable = LayerDrawable(arrayOf<Drawable>(shapeStroke, shapeDrawable))
            drawable.setLayerInset(
                0,
                unPressedGlowSize,
                unPressedGlowSize,
                unPressedGlowSize,
                unPressedGlowSize
            )
            val innerShapeSize = unPressedGlowSize + strokeWidth
            drawable.setLayerInset(
                1,
                innerShapeSize,
                innerShapeSize,
                innerShapeSize,
                innerShapeSize
            )
            return drawable
        }
    }
}